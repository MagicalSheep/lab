package cn.magicalsheep.myapplication.data.entity

import java.util.Date

data class Refer(
    val sources: List<String>,
    val license: List<String>
)

data class Report(
    val code: String,
    val updateTime: Date,
    val fxLink: String,
    val daily: List<Weather>,
    val refer: Refer
)
