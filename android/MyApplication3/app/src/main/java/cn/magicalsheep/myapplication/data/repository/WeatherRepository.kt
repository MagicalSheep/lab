package cn.magicalsheep.myapplication.data.repository

import cn.magicalsheep.myapplication.data.entity.Report
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val baseUrl = "https://devapi.qweather.com/v7/weather/"
const val key = "1f777e7388c1467b99fd51bc5500ce6e"

val weatherRepository: WeatherRepository = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(WeatherRepository::class.java);

interface WeatherRepository {

    @GET("3d")
    fun get3dWeather(@Query("location") location: String, @Query("key") key: String): Call<Report>

    @GET("7d")
    suspend fun get7dWeather(@Query("location") location: String, @Query("key") key: String): Report

}