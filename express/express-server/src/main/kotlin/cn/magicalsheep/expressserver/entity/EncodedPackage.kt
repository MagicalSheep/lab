package cn.magicalsheep.expressserver.entity

import cn.magicalsheep.expressserver.entity.vo.PackageVo
import com.google.gson.Gson
import org.apache.tomcat.util.codec.binary.Base64
import java.io.Serializable
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

data class EncodedPackage(
    val id: Long,
    val iv: String,
    val data: String
) : Serializable {

    fun decode(secret: String): PackageVo {
        val rawIv = Base64.decodeBase64(iv)
        val rawData = Base64.decodeBase64(data)
        val rawSecret = Base64.decodeBase64(secret)
        val key = SecretKeySpec(rawSecret, "AES")
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(rawIv))
        val plaintext = String(cipher.doFinal(rawData), StandardCharsets.UTF_8)
        return Gson().fromJson(plaintext, PackageVo::class.java)
    }
}
