package cn.magicalsheep.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class Photo(
    val id: Int,
    val sol: Int,
    val camera: Camera,
    @SerializedName("img_src")
    val imgSrc: String,
    @SerializedName("earth_date")
    val earthDate: String,
    val rover: Rover,
)
