package com.example.ifila_app

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface MainAPI {

    @POST("usuarios?role=user")
    suspend fun createUser(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("auth/login")
    suspend fun loginUser(@Body requestBody: RequestBody): Response<ResponseBody>

    //@GET("estabelecimentos/${id}")
    //suspend fun getBusiness(@Body requestBody: RequestBody, id: String): Response<ResponseBody>

    @GET("estabelecimentos/{id}")
    suspend fun getBusiness(@Path("id") id: String, @Header("Authorization") auth:String): Response<ResponseBody>

    @GET("usuarios/me")
    suspend fun getUserMe(@Header("Authorization") auth:String): Response<ResponseBody>
}