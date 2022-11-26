package cn.magicalsheep.expressui.data.entity

import android.util.Base64
import com.google.gson.GsonBuilder
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

    fun decode(secret: String): Package {
        val rawIv = Base64.decode(iv.toByteArray(), Base64.DEFAULT)
        val rawData = Base64.decode(data.toByteArray(), Base64.DEFAULT)
        val rawSecret = Base64.decode(secret.toByteArray(), Base64.DEFAULT)
        val key = SecretKeySpec(rawSecret, "AES")
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(rawIv))
        val plaintext = String(cipher.doFinal(rawData), StandardCharsets.UTF_8)
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        return gson.fromJson(plaintext, Package::class.java)
    }

}