package com.example.ifila_app

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MainAPI {

    @POST("usuarios?role=user")
    suspend fun createUser(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("auth/login")
    suspend fun loginUser(@Body requestBody: RequestBody): Response<ResponseBody>

}