package cn.magicalsheep.expressserver.controller

import cn.magicalsheep.expressserver.checkPerm
import cn.magicalsheep.expressserver.entity.DefaultException
import cn.magicalsheep.expressserver.entity.Result
import cn.magicalsheep.expressserver.entity.UnauthorizedException
import cn.magicalsheep.expressserver.entity.pojo.User
import cn.magicalsheep.expressserver.entity.dto.UserDto
import cn.magicalsheep.expressserver.entity.vo.UserVo
import cn.magicalsheep.expressserver.jwtSecret
import cn.magicalsheep.expressserver.repository.PackageRepository
import cn.magicalsheep.expressserver.repository.UserRepository
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.password4j.Hash
import com.password4j.Password
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
class UserController(
    private val userRepository: UserRepository,
    private val packageRepository: PackageRepository,
) {
    @PostMapping("/user/login")
    fun login(@RequestBody req: UserDto): Result {
        if (req.pwd == null)
            throw DefaultException("密码不能为空")
        val user = userRepository.findUserByAccount(req.account) ?: throw DefaultException("用户不存在")
        if (!Password.check(req.pwd, user.pwd).withBcrypt())
            throw DefaultException("密码错误")
        val algorithm: Algorithm = Algorithm.HMAC512(jwtSecret)
        val token = JWT.create().withClaim("uid", UUID.randomUUID().toString())
            .withClaim("account", user.account)
            .withClaim("role", user.role)
            .sign(algorithm)
        return Result(HttpStatus.OK.value(), user.role, token)
    }

    @PostMapping("/user/register")
    fun register(@RequestBody req: UserDto): Result {
        if (req.pwd.isNullOrEmpty())
            throw DefaultException("密码不能为空")
        if (userRepository.findUserByAccount(req.account) != null) {
            throw DefaultException("用户已存在")
        }
        val hash: Hash = Password.hash(req.pwd).withBcrypt()
        val user = User(req)
        user.pwd = hash.result
        val finalData = userRepository.save(user)
        return Result(HttpStatus.OK.value(), "注册成功", UserVo(finalData))
    }

    @GetMapping("/users")
    fun getUsers(
        @RequestHeader("Authorization") token: String,
        @RequestParam("page") page: Int,
        @RequestParam("page_size") pageSize: Int
    ): Result {
        checkPerm(token, setOf("admin"))
        val res = userRepository.findAll(PageRequest.of(page - 1, pageSize, Sort.by("id").ascending()))
        val ret = res.toList().filterNotNull().map { UserVo(it) }
        return Result(HttpStatus.OK.value(), "获取用户信息列表成功", ret)
    }

    @PostMapping("/user")
    fun updateUser(@RequestHeader("Authorization") token: String, @RequestBody req: UserDto): Result {
        checkPerm(token, setOf("default", "admin"))
        val user = userRepository.findUserByAccount(req.account) ?: throw DefaultException("用户不存在")
        user.updateFromDto(req)
        if (req.pwd != null) {
            val hash: Hash = Password.hash(req.pwd).withBcrypt()
            user.pwd = hash.result
        }
        val finalData = userRepository.saveAndFlush(user)
        return Result(HttpStatus.OK.value(), "更新用户信息成功", UserVo(finalData))
    }

    @PostMapping("/user/role")
    fun changeRole(
        @RequestHeader("Authorization") token: String,
        @RequestParam("account") account: String,
        @RequestParam("role") role: String
    ): Result {
        checkPerm(token, setOf("admin"))
        val user = userRepository.findUserByAccount(account) ?: throw DefaultException("用户不存在")
        if (role != "admin" && role != "default")
            throw DefaultException("角色不存在")
        user.role = role
        val finalData = userRepository.saveAndFlush(user)
        return Result(HttpStatus.OK.value(), "更改用户角色成功", UserVo(finalData))
    }

    @PutMapping("/user/permission")
    fun addPermission(
        @RequestHeader("Authorization") token: String,
        @RequestParam("account") account: String,
        @RequestParam("package_id") packageId: Long
    ): Result {
        checkPerm(token, setOf("admin"))
        val user = userRepository.findUserByAccount(account) ?: throw DefaultException("用户不存在")
        val res = packageRepository.findById(packageId)
        if (res.isEmpty) {
            throw DefaultException("快递不存在")
        }
        val pack = res.get()
        user.accessPackages.add(pack)
        userRepository.saveAndFlush(user)
        return Result(HttpStatus.OK.value(), "增加用户权限成功")
    }

    @DeleteMapping("/user/permission")
    fun removePermission(
        @RequestHeader("Authorization") token: String,
        @RequestParam("account") account: String,
        @RequestParam("package_id") packageId: Long
    ): Result {
        checkPerm(token, setOf("admin"))
        val user = userRepository.findUserByAccount(account) ?: throw DefaultException("用户不存在")
        val res = packageRepository.findById(packageId)
        if (res.isEmpty) {
            throw DefaultException("快递不存在")
        }
        val pack = res.get()
        user.accessPackages.remove(pack)
        userRepository.saveAndFlush(user)
        return Result(HttpStatus.OK.value(), "移除用户权限成功")
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