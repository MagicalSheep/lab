package cn.magicalsheep.expressserver.controller

import cn.magicalsheep.expressserver.checkPerm
import cn.magicalsheep.expressserver.entity.*
import cn.magicalsheep.expressserver.entity.EncodedPackage
import cn.magicalsheep.expressserver.entity.dto.PackageDto
import cn.magicalsheep.expressserver.entity.pojo.Package
import cn.magicalsheep.expressserver.entity.vo.PackageVo
import cn.magicalsheep.expressserver.repository.PackageRepository
import cn.magicalsheep.expressserver.repository.UserRepository
import cn.magicalsheep.expressserver.toImage
import cn.magicalsheep.expressserver.verify
import com.google.gson.GsonBuilder
import io.nayuki.qrcodegen.QrCode
import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@RestController
class PackageController(
    private val packageRepository: PackageRepository,
    private val userRepository: UserRepository
) {

    @PutMapping("/admin/package")
    fun adminAddPackage(@RequestHeader("Authorization") token: String, @RequestBody req: PackageDto): Result {
        checkPerm(token, setOf("admin"))
        if (req.senderName == null || req.senderAddress == null || req.senderPhone == null ||
            req.receiverName == null || req.receiverAddress == null || req.receiverPhone == null ||
            req.description == null || req.weight == null || req.freight == null ||
            req.position == null
        ) {
            throw DefaultException("部分字段不能为空")
        }
        if (req.isRecv != null && req.isRecv && req.recvTime == null) {
            throw DefaultException("签收时间不能为空")
        }
        val finalData = packageRepository.save(Package(req))
        return Result(HttpStatus.OK.value(), "添加快递成功", PackageVo(finalData))
    }

    @GetMapping("/admin/packages")
    fun adminGetPackages(
        @RequestHeader("Authorization") token: String,
        @RequestParam("page") page: Int,
        @RequestParam("page_size") pageSize: Int
    ): Result {
        checkPerm(token, setOf("admin"))
        val res = packageRepository.findAll(PageRequest.of(page - 1, pageSize, Sort.by("id").ascending()))
        val ret = res.toList().filterNotNull().map { PackageVo(it) }
        return Result(HttpStatus.OK.value(), "获取快递信息列表成功", ret)
    }

    @PostMapping("/admin/package")
    fun adminUpdatePackage(@RequestHeader("Authorization") token: String, @RequestBody req: PackageDto): Result {
        checkPerm(token, setOf("admin"))
        if (req.id == null) {
            throw DefaultException("快递ID不能为空")
        }
        if (req.isRecv != null && req.isRecv && req.recvTime == null) {
            throw DefaultException("签收时间不能为空")
        }

        val res = packageRepository.findById(req.id)
        if (res.isEmpty) {
            throw DefaultException("快递不存在")
        }
        val pack = res.get()
        pack.updateFromDto(req)
        val finalData = packageRepository.save(pack)
        return Result(HttpStatus.OK.value(), "更新快递信息成功", PackageVo(finalData))
    }

    @PostMapping("/package/decode")
    fun tryDecodePackage(
        @RequestHeader("Authorization") token: String,
        @RequestParam("secret") secret: String,
        @RequestBody req: EncodedPackage
    ): Result {
        checkPerm(token, setOf("default", "admin"))
        return Result(HttpStatus.OK.value(), "解密快递成功", req.decode(secret))
    }

    @GetMapping("/package", produces = ["text/html;charset=utf-8"])
    fun getPackage(@RequestParam("package_id") packageId: Long): String {
        val res = packageRepository.findById(packageId)
        if (res.isEmpty) {
            throw DefaultException("快递不存在")
        }
        val pack = res.get()
        val gson = GsonBuilder().disableHtmlEscaping().create()
        val data = gson.toJson(pack.encode())
        val qr0 = QrCode.encodeText(data, QrCode.Ecc.MEDIUM)
        val image = toImage(qr0, 4, 0)
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(image, "png", outputStream)
        val bytes = outputStream.toByteArray()
        val retImg = Base64.encodeBase64String(bytes)
        return "<img src=\"data:image/png;base64,$retImg\">"
//        return Result(HttpStatus.OK.value(), "Ok", "data:image/png;base64,$retImg")
    }

    @GetMapping("/package/secret")
    fun getSecret(@RequestHeader("Authorization") token: String, @RequestParam("package_id") packageId: Long): Result {
        checkPerm(token, setOf("default", "admin"))
        // Get user from token
        val account = verify(token).claims["account"]?.asString() ?: throw DefaultException("无效令牌")
        val user = userRepository.findUserByAccount(account) ?: throw DefaultException("用户不存在")
        val res = packageRepository.findById(packageId)
        if (res.isEmpty) {
            throw DefaultException("快递不存在")
        }
        val pack = res.get()
        if (!pack.accessUsers.contains(user)) {
            throw DefaultException("你没有查看该快递信息的权限")
        }
        return Result(HttpStatus.OK.value(), "获取快递解密密钥成功", pack.secret)
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = [DefaultException::class, Exception::class])
    fun handleDefaultException(ex: Exception): Result {
        return Result(HttpStatus.FORBIDDEN.value(), ex.message ?: "未知错误")
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = [UnauthorizedException::class])
    fun handleUnauthorizedException(ex: UnauthorizedException): Result {
        return Result(HttpStatus.UNAUTHORIZED.value(), ex.message ?: "未知错误")
    }
}