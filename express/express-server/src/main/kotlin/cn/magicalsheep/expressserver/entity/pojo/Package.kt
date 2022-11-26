package cn.magicalsheep.expressserver.entity.pojo

import cn.magicalsheep.expressserver.entity.EncodedPackage
import cn.magicalsheep.expressserver.entity.dto.PackageDto
import cn.magicalsheep.expressserver.entity.vo.PackageVo
import com.google.gson.GsonBuilder
import jakarta.persistence.*
import org.apache.tomcat.util.codec.binary.Base64
import java.io.Serializable
import java.nio.charset.StandardCharsets
import java.security.AlgorithmParameters
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


@Entity
@Table(name = "package")
class Package(
    @Column(name = "sender_name", nullable = false)
    var senderName: String,
    @Column(name = "sender_address", nullable = false)
    var senderAddress: String,
    @Column(name = "sender_phone", nullable = false)
    var senderPhone: String,
    @Column(name = "receiver_name", nullable = false)
    var receiverName: String,
    @Column(name = "receiver_address", nullable = false)
    var receiverAddress: String,
    @Column(name = "receiver_phone", nullable = false)
    var receiverPhone: String,
    @Column(name = "description", nullable = false)
    var description: String,
    @Column(name = "weight", nullable = false)
    var weight: Double,
    @Column(name = "freight", nullable = false)
    var freight: Double,
    @Column(name = "position", nullable = false)
    var position: String,
    @Column(name = "send_time", nullable = false)
    var sendTime: Date = Date(),
    @Column(name = "is_recv", nullable = false)
    var isRecv: Boolean = false,
    @Column(name = "recv_time", nullable = true)
    var recvTime: Date? = null,
) : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @ManyToMany(mappedBy = "accessPackages")
    var accessUsers: MutableSet<User> = mutableSetOf()

    @Column(name = "secret", nullable = false)
    var secret: String

    init {
        val generator: KeyGenerator = KeyGenerator.getInstance("AES")
        generator.init(256, SecureRandom())
        secret = Base64.encodeBase64String(generator.generateKey().encoded)
    }

    constructor(pack: PackageDto) : this(
        pack.senderName ?: "default",
        pack.senderAddress ?: "default",
        pack.senderPhone ?: "default",
        pack.receiverName ?: "default",
        pack.receiverAddress ?: "default",
        pack.receiverPhone ?: "default",
        pack.description ?: "default",
        pack.weight ?: 0.0,
        pack.freight ?: 0.0,
        pack.position ?: "default",
        pack.sendTime ?: Date(),
        pack.isRecv ?: false,
        if (pack.isRecv != null && pack.isRecv) pack.recvTime else null
    )

    fun updateFromDto(source: PackageDto) {
        this.senderName = source.senderName ?: this.senderName
        this.senderAddress = source.senderAddress ?: this.senderAddress
        this.senderPhone = source.senderPhone ?: this.senderPhone
        this.receiverName = source.receiverName ?: this.receiverName
        this.receiverAddress = source.receiverAddress ?: this.receiverAddress
        this.receiverPhone = source.receiverPhone ?: this.receiverPhone
        this.description = source.description ?: this.description
        this.weight = source.weight ?: this.weight
        this.freight = source.freight ?: this.freight
        this.position = source.position ?: this.position
        this.sendTime = source.sendTime ?: this.sendTime
        this.isRecv = source.isRecv ?: this.isRecv
        this.recvTime = source.recvTime
    }

    /**
     * Make PackageVo object and translate to JSON text.
     * Then encode it using AES.
     */
    fun encode(): EncodedPackage {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        val obj: String = gson.toJson(PackageVo(this))
        val bytes = Base64.decodeBase64(secret)
        val key = SecretKeySpec(bytes, "AES")
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val params: AlgorithmParameters = cipher.parameters
        val iv: ByteArray = params.getParameterSpec(IvParameterSpec::class.java).iv
        val ciphertext: ByteArray = cipher.doFinal(obj.toByteArray(StandardCharsets.UTF_8))
        val ivStr = Base64.encodeBase64String(iv)
        val finalText = Base64.encodeBase64String(ciphertext)
        return EncodedPackage(id!!, ivStr, finalText)
    }
}