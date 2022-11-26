package cn.magicalsheep.expressserver

import cn.magicalsheep.expressserver.entity.DefaultException
import cn.magicalsheep.expressserver.entity.UnauthorizedException
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import io.nayuki.qrcodegen.QrCode
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.awt.image.BufferedImage


@SpringBootApplication
class ExpressServerApplication

const val jwtSecret = "Hello-World"

fun toImage(qr: QrCode, scale: Int, border: Int): BufferedImage {
    return toImage(qr, scale, border, 0xFFFFFF, 0x000000)
}

/**
 * Returns a raster image depicting the specified QR Code, with
 * the specified module scale, border modules, and module colors.
 *
 * For example, scale=10 and border=4 means to pad the QR Code with 4 light border
 * modules on all four sides, and use 10&#xD7;10 pixels to represent each module.
 * @param qr the QR Code to render (not `null`)
 * @param scale the side length (measured in pixels, must be positive) of each module
 * @param border the number of border modules to add, which must be non-negative
 * @param lightColor the color to use for light modules, in 0xRRGGBB format
 * @param darkColor the color to use for dark modules, in 0xRRGGBB format
 * @return a new image representing the QR Code, with padding and scaling
 * @throws NullPointerException if the QR Code is `null`
 * @throws IllegalArgumentException if the scale or border is out of range, or if
 * {scale, border, size} cause the image dimensions to exceed Integer.MAX_VALUE
 */
fun toImage(qr: QrCode, scale: Int, border: Int, lightColor: Int, darkColor: Int): BufferedImage {
    require(!(scale <= 0 || border < 0)) { "Value out of range" }
    require(!(border > Int.MAX_VALUE / 2 || qr.size + border * 2L > Int.MAX_VALUE / scale)) { "Scale or border too large" }
    val result =
        BufferedImage((qr.size + border * 2) * scale, (qr.size + border * 2) * scale, BufferedImage.TYPE_INT_RGB)
    for (y in 0 until result.height) {
        for (x in 0 until result.width) {
            val color = qr.getModule(x / scale - border, y / scale - border)
            result.setRGB(x, y, if (color) darkColor else lightColor)
        }
    }
    return result
}

fun verify(token: String): DecodedJWT {
    try {
        val algorithm: Algorithm = Algorithm.HMAC512(jwtSecret)
        val verifier = JWT.require(algorithm).build()
        return verifier.verify(token)
    } catch (ex: Exception) {
        throw UnauthorizedException(ex.message ?: "未知错误")
    }
}

fun checkPerm(token: String, perm: Set<String>) {
    val role = verify(token).claims["role"]?.asString()
    if (!perm.contains(role))
        throw DefaultException("用户权限不足")
}

fun main(args: Array<String>) {
    runApplication<ExpressServerApplication>(*args)
}
