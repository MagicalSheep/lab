package cn.magicalsheep.expressui.data.entity

import java.io.Serializable
import java.util.*

data class Package(
    val id: Long,
    var senderName: String,
    var senderAddress: String,
    var senderPhone: String,
    var receiverName: String,
    var receiverAddress: String,
    var receiverPhone: String,
    var description: String,
    var weight: Double,
    var freight: Double,
    var position: String,
    var sendTime: Date,
    var isRecv: Boolean,
    var recvTime: Date? = null
) : Serializable