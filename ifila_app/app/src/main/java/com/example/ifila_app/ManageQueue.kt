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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit

class ManageQueue : AppCompatActivity() {

    private lateinit var binding: ActivityManageQueueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageQueueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonFecharFila.setOnClickListener { goToEstabWithouQueue() }

    }


    fun goToEstabWithouQueue(){
        startCloseQueue()
//        val context = binding.root.context
//       val intent = Intent(context, EstabWithoutQueueScreen::class.java)//Coloquei pra ir pra tela de ir pra Estabelecimento sem fila
//        context.startActivity(intent)
    }

    fun startCloseQueue(){

        val lastIntent = intent
        val extras = lastIntent.extras
        val token = extras?.get("token").toString()

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