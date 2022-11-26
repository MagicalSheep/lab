package cn.magicalsheep.expressui.data.repository

import cn.magicalsheep.expressui.baseUrl
import cn.magicalsheep.expressui.data.entity.Package
import cn.magicalsheep.expressui.data.entity.Result
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

var packageRepositoryGson: Gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd HH:mm:ss")
    .create()

val packageRepository: PackageRepository = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create(packageRepositoryGson))
    .build()
    .create(PackageRepository::class.java);

interface PackageRepository {

    @GET("package/secret")
    fun getSecret(
        @Header("Authorization") token: String,
        @Query("package_id") packageId: Long
    ): Call<Result<String>>

    @GET("admin/packages")
    suspend fun getPackages(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Result<List<Package>>

    @POST("admin/package")
    suspend fun updatePackage(
        @Header("Authorization") token: String,
        @Body pack: Package
    ): Result<Package>

    @PUT("admin/package")
    suspend fun addPackage(
        @Header("Authorization") token: String,
        @Body pack: Package
    ): Result<Package>

}