package cn.magicalsheep.expressserver.entity.dto

import java.io.Serializable
import java.util.*

data class PackageDto(
    val id: Long? = null,
    val senderName: String? = null,
    val senderAddress: String? = null,
    val senderPhone: String? = null,
    val receiverName: String? = null,
    val receiverAddress: String? = null,
    val receiverPhone: String? = null,
    val description: String? = null,
    val weight: Double? = null,
    val freight: Double? = null,
    val position: String? = null,
    val sendTime: Date? = null,
    val isRecv: Boolean? = null,
    val recvTime: Date? = null
) : Serializable