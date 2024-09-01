package com.example.thanachaiandroid

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("computers")
    fun addComputer(
        @Part("brand_name") brandName: RequestBody,
        @Part("model_name") modelName: RequestBody,
        @Part("serial_number") serialNumber: RequestBody,
        @Part("stock_quantity") stockQuantity: RequestBody,
        @Part("price") price: RequestBody,
        @Part("cpu_speed") cpuSpeed: RequestBody,
        @Part("memory_size") memorySize: RequestBody,
        @Part("hard_disk_size") hardDiskSize: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<Void>

    @GET("computers")
    fun getComputers(): Call<List<Computer>>
}
