package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.example.ifila_app.databinding.ActivityCreateQueueScreenBinding
import com.example.ifila_app.databinding.ActivityEstabWithoutQueueScreenBinding
import com.example.ifila_app.databinding.ActivityRegisterScreen1Binding
import com.google.android.material.timepicker.TimeFormat
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

class CreateQueueScreen : AppCompatActivity() {
    private lateinit var binding: ActivityCreateQueueScreenBinding
    lateinit var token:String
    lateinit var business_name:String
    lateinit var business_code:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateQueueScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonFinalizar.isEnabled = false

        //binding.editTextLimiteTempo.addTextChangedListener()

        limitPeopleFocusListener()
        limitTimeFocusListener()
        token = intent.extras?.get("token").toString()
        business_name = intent.extras?.get("business_name").toString()
        business_code = intent.extras?.get("business_code").toString()

        binding.buttonFinalizar.setOnClickListener { goToManageQueue() }
        binding.buttonCancelar.setOnClickListener { goToCreateQueue() }
    }

    fun goToManageQueue(){
        startCreateQueue()
//        val context = binding.root.context
//        val intent = Intent(context, ManageQueue::class.java)
//        context.startActivity(intent)
    }

    private fun goToCreateQueue(){
        val context = binding.root.context
        val intent = Intent(context, EstabWithoutQueueScreen::class.java)
        intent.putExtra("token", token)
        intent.putExtra("business_name", business_name)
        intent.putExtra("business_code", business_code)
        finish()
        context.startActivity(intent)
    }

    fun startCreateQueue(){
        val limit_people = binding.editTextLimitePessoas.text.toString()
        val limit_time = binding.editTextLimiteTempo.text.toString()
        val jsonObject = JSONObject()

        jsonObject.put("capacidadeMaxima", limit_people)
        jsonObject.put("horarioMaximoEntrada", limit_time)

        Log.d("TEST",limit_people  + "||" + limit_time)
        Log.d("TEST TOKEN",token)

        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterScreen2.URL_SETUP_USER)
            .build()
        val service = retrofit.create(MainAPI::class.java)

//        Log.d("ANTES", token)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.createQueue("Bearer $token",requestBody)

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
                    val intent = Intent(context, ManageQueue::class.java)

                    intent.putExtra("token", token)
                    intent.putExtra("business_name", business_name)
                    intent.putExtra("business_code", business_code)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    finish()
                    context.startActivity(intent)


                } else {
                    Log.d("ERROR","FILA JÁ ESTÁ ABERTA")
                }
            }
        }
    }

    private fun limitPeopleFocusListener( )
    {
        binding.editTextLimitePessoas.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                enableButton()
            }
        })
    }

    private fun limitTimeFocusListener( )
    {
        binding.editTextLimiteTempo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                enableButton()
            }
        })
    }

    private fun enableButton() {
        binding.buttonFinalizar.isEnabled =
            (
                    binding.editTextLimitePessoas.text != null &&
                            binding.editTextLimiteTempo.text != null
                    )
    }


}