package com.example.ifila_app

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class BackManager {
    companion object Requester {
        private val client = OkHttpClient()
        private val mediaType = "application/json; charset=utf-8".toMediaType()

        fun postRequest(jsonObject: JSONObject): Response? {
            val body = jsonObject.toString().toRequestBody(mediaType)
            val request: Request = Request.Builder()
                .url("http://YOUR_URL/")
                .post(body)
                .build()
            var response: Response? = null
            try {
                response = client.newCall(request).execute()
                return response
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        }
    }
}