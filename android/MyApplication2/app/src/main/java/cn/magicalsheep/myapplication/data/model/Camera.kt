package cn.magicalsheep.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class Camera(
    val id: Int,
    val name: String,
    @SerializedName("rover_id")
    val roverId: Int,
    @SerializedName("full_name")
    val fullName: String,
)
