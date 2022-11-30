package cn.magicalsheep.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class Rover(
    val id: Int,
    val name: String,
    @SerializedName("landing_date")
    val landingDate: String,
    @SerializedName("launch_date")
    val launchDate: String,
    val status: String,
)
