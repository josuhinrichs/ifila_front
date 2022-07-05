package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ifila_app.databinding.ActivityCreateQueueScreenBinding
import com.example.ifila_app.databinding.ActivityManageQueueBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class ManageQueue : AppCompatActivity() {

    private lateinit var binding: ActivityManageQueueBinding
    lateinit var token:String
    lateinit var business_name : String
    lateinit var business_code: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageQueueBinding.inflate(layoutInflater)
        setContentView(binding.root)
        token = intent.extras?.get("token").toString()
        business_name = intent.extras?.get("business_name").toString()
        business_code = intent.extras?.get("business_code").toString()
        binding.buttonFecharFila.setOnClickListener { goToEstabWithouQueue() }
        binding.buttonChamar.setOnClickListener { goToCallNextUser() }
        binding.buttonAtender.setOnClickListener { goToAttendNextUser() }
        binding.buttonPular.setOnClickListener { goToSkipUser() }
        binding.buttonAtender.isEnabled = false

        binding.nomeEstabelecimento.text = business_name
        binding.textCodigoFila.text = business_code
    }

    private fun goToCallNextUser(){
        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterScreen2.URL_SETUP_USER)
            .build()
        val service = retrofit.create(MainAPI::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.callNextUser("Bearer $token")

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )
                    Log.d("Pretty Printed JSON :", prettyJson)
                    binding.buttonChamar.isEnabled = false
                    binding.buttonAtender.isEnabled = false

                } else {
                    Log.d("ERROR", token)
                }
            }
        }
    }

    fun goToAttendNextUser(){
        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterScreen2.URL_SETUP_USER)
            .build()
        val service = retrofit.create(MainAPI::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.attendNextUser("0","Bearer $token")

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )
                    Log.d("Attend Pretty Printed JSON :", prettyJson)
                    binding.buttonChamar.isEnabled = true
                    binding.buttonAtender.isEnabled = false

                } else {
                    Log.d("ERROR attend", token)
                }
            }
        }
    }

    fun goToSkipUser(){
        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterScreen2.URL_SETUP_USER)
            .build()
        val service = retrofit.create(MainAPI::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.attendNextUser("1","Bearer $token")

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )
                    Log.d(" Skip Pretty Printed JSON :", prettyJson)
                    binding.buttonChamar.isEnabled = true
                    binding.buttonAtender.isEnabled = false

                } else {
                    Log.d("ERROR skip", token)
                }
            }
        }
    }

    fun goToEstabWithouQueue(){
        startCloseQueue()
//        val context = binding.root.context
//        val intent = Intent(context, EstabWithoutQueueScreen::class.java)//Coloquei pra ir pra tela de ir pra Estabelecimento sem fila
//        intent.putExtra("token", token)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        finish()
//        context.startActivity(intent)
    }

    fun startCloseQueue(){
        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterScreen2.URL_SETUP_USER)
            .build()
        val service = retrofit.create(MainAPI::class.java)

        Log.d("ANTES", token)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.closeQueue("Bearer $token")

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    // Convert raw JSON to pretty JSON using GSON library

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )

                    Log.d("TEST 1",prettyJson)

                    val context = binding.root.context
                    val intent = Intent(context, EstabWithoutQueueScreen::class.java)

                    intent.putExtra("token", token)
                    intent.putExtra("business_name", business_name)
                    intent.putExtra("business_code", business_code)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    finish()
                    context.startActivity(intent)

                } else {
                    Log.d("TEST","ERRO ERRO ")
                }
            }
        }
    }
}