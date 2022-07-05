package com.example.ifila_app

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface MainAPI {

    @POST("usuarios?role=usuario")
    suspend fun createUser(@Body requestBody: RequestBody): Response<ResponseBody>

    @POST("auth/login")
    suspend fun loginUser(@Body requestBody: RequestBody): Response<ResponseBody>

    //@GET("estabelecimentos/${id}")
    //suspend fun getBusiness(@Body requestBody: RequestBody, id: String): Response<ResponseBody>

    @GET("estabelecimentos/me")
    suspend fun getBusinessMe(@Header("Authorization") auth:String): Response<ResponseBody>

    @GET("estabelecimentos/codigo/{id}")
    suspend fun getBusiness(@Path("id") id: String, @Header("Authorization") auth:String): Response<ResponseBody>

    @GET("usuarios/me")
    suspend fun getUserMe(@Header("Authorization") auth:String): Response<ResponseBody>

    @PUT("fila/entrar/{code}")
    suspend fun enterQueue(@Path("code") code: String, @Query("Tipo de Fila") priority:String, @Header("Authorization") auth:String): Response<ResponseBody>

    @POST("fila/abrir")
    suspend fun createQueue(@Header("Authorization") auth:String, @Body requestBody: RequestBody): Response<ResponseBody>

    @PUT("fila/sair")
    suspend fun leaveQueue(@Header("Authorization") auth:String): Response<ResponseBody>

    @PUT("fila/fechar")
    suspend fun closeQueue(@Header("Authorization") auth:String): Response<ResponseBody>

    @PUT("fila/chamarcliente")
    suspend fun callNextUser(@Header("Authorization") auth:String): Response<ResponseBody>

    @PUT("fila/atendercliente")
    suspend fun attendNextUser(@Query("pular") pular: String, @Header("Authorization") auth:String): Response<ResponseBody>

    @PUT("fila/confirmarpresenca")
    suspend fun confirmPresence(@Header("Authorization") auth:String): Response<ResponseBody>
    
}