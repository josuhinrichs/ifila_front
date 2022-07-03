package com.example.ifila_app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.ifila_app.databinding.ActivityEnterCodeScreenBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit


class EnterCodeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityEnterCodeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterCodeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonBuscarEst.setOnClickListener { startCodRequest() }

    }


    private fun startCodRequest(){

        val context = binding.root.context
        val lastIntent = intent
        val extras = lastIntent.extras
        val token = extras?.get("token").toString()

        sendCodRequest(token)
    }

    private fun sendCodRequest(token: String){

        val jsonObject = JSONObject()
        jsonObject.put("token", token)

        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterScreen2.URL_SETUP_USER)
            .build()
        val service = retrofit.create(MainAPI::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getBusiness(binding.editTextCode.text.toString(), "Bearer $token")

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    // Convert raw JSON to pretty JSON using GSON library

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )

//                    val parts = prettyJson
//                        .replace("\n","")
//                        .replace("{","")
//                        .replace("}","")
//                        .replace("\"","")
//                        .replace(" ","")
//
//                    val map = parts.split(",").associate {
//                        val(left, right) = it.split(":")
//                        left to right
//                    }.toMutableMap()
//
//                    val id = map["id"] ?: ""
//                    val nome = map["nome"] ?: ""
//                    val endereco = map["endereco"] ?: ""
//                    val telefone = map["telefone"] ?: ""
//                    val cnpj = map["cnpj"] ?: ""
//                    val descricacao = map["descricacao"] ?: ""
//                    val horarioAbertura = map["horarioAbertura"] ?: ""
//                    val horarioFechamento = map["horarioFechamento"] ?: ""
//                    val dataDeCriacao = map["dataDeCriacao"] ?: ""
//                    val statusFila = map["statusFila"] ?: ""

                    Log.d("josue",prettyJson)

                } else {
                    //binding.textCodigoInvalido.visibility = View.VISIBLE
                    Log.d("josue","num deu bom")
                }
            }
        }

    }

}