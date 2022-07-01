package com.example.ifila_app

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException


class BackManager {
    companion object Requester {
        private val client = OkHttpClient()
        private val mediaType = "application/json; charset=utf-8".toMediaType()

        fun postRequest(jsonObject: JSONObject): Response? {
            val body = jsonObject.toString().toRequestBody(mediaType)
            val request: Request = Request.Builder()
                .url("http://ifila.com.br:8000/usuarios?role=user")
                .post(body)
                .build()
            var responseRequest: Response? = null

            client.newCall(request).enqueue(object :Callback{
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    responseRequest = response
                }
            })
            return responseRequest

            }
//            try {
//                client.newCall(request).enqueue(object : Callback {
//                    override fun onResponse(call: Call, response: Response
//                    ) {
//                        responseRequest = response
//                    }
//
//                    override fun onFailure(call: Call, e: IOException) {
//                    }
//                })
//                return responseRequest?.code
//            } catch (e: IOException) {
//                e.printStackTrace()
//                return null
//            }

    }
}