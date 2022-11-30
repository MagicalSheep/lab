package cn.magicalsheep.myapplication.data

import cn.magicalsheep.myapplication.data.model.Photos
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

const val baseUrl = "https://api.nasa.gov/"
val photoService: PhotoService = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(PhotoService::class.java);

interface PhotoService {
    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun allPhotos(@Query("sol") sol: Int, @Query("api_key") apiKey: String): Call<Photos>

    @GET
    fun downloadPhoto(@Url url: String): Call<ResponseBody>
}