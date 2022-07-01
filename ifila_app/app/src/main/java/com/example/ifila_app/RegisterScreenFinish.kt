package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonToken
import android.util.Log
import com.example.ifila_app.databinding.ActivityRegisterScreen2Binding
import com.example.ifila_app.databinding.ActivityRegisterScreenFinishBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit

class RegisterScreenFinish : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterScreenFinishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterScreenFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonVamosla.setOnClickListener { startLogin() }
    }

    private fun startLogin(){
        val context = binding.root.context
        val lastIntent = intent
        val extras = lastIntent.extras
        val email= extras?.get("email").toString()
        val password = extras?.get("password").toString()

        sendLoginRequest(email, password)
    }

    private fun sendLoginRequest(email: String, password:String){
        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("senha", password)

        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterScreen2.URL_SETUP_USER)
            .build()
        val service = retrofit.create(MainAPI::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.loginUser(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    // Convert raw JSON to pretty JSON using GSON library

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )
                    val parts = prettyJson
                        .replace("\n","")
                        .replace("{","")
                        .replace("}","")
                        .replace("\"","")
                        .replace(" ","")

                    val map = parts.split(",").associate {
                        val(left, right) = it.split(":")
                        left to right
                    }.toMutableMap()
                    val token = map["token"] ?: ""
                    val role = map["role"] ?: ""
                    loginUser(token)
                } else {
                }
            }
        }

    }

    fun loginUser(token:String){
        val context = binding.root.context
        val intent = Intent(context, EnterCodeScreen::class.java)

        intent.putExtra("token", token)
        context.startActivity(intent)
    }

}

class SimpleEntity(val token: String, val role: String){

}