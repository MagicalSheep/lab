package cn.magicalsheep.expressserver.entity.vo

import cn.magicalsheep.expressserver.entity.pojo.Package
import java.io.Serializable
import java.util.*

data class PackageVo(
    val id: Long,
    val senderName: String,
    val senderAddress: String,
    val senderPhone: String,
    val receiverName: String,
    val receiverAddress: String,
    val receiverPhone: String,
    val description: String,
    val weight: Double,
    val freight: Double,
    val position: String,
    val sendTime: Date,
    val isRecv: Boolean,
    val recvTime: Date? = null
) : Serializable {
    constructor(pack: Package) : this(
        pack.id ?: throw Exception("Impossible"),
        pack.senderName,
        pack.senderAddress,
        pack.senderPhone,
        pack.receiverName,
        pack.receiverAddress,
        pack.receiverPhone,
        pack.description,
        pack.weight,
        pack.freight,
        pack.position,
        pack.sendTime,
        pack.isRecv,
        pack.recvTime
    )
}
