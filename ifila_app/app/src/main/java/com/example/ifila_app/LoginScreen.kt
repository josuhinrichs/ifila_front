package com.example.ifila_app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.JsonToken
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.ifila_app.databinding.ActivityLoginScreenBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.regex.Pattern

class LoginScreen : AppCompatActivity() {
    private lateinit var binding: ActivityLoginScreenBinding
    var business_name = ""
    var business_code = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailFocusListener()
        passwdFocusListener()

        binding.buttonEntrar.setOnClickListener { startLogin() }
        binding.buttonCadastrar.setOnClickListener { signUp() }
    }

    private fun startLogin(){
        val email= binding.editTextEmail.text.toString()
        val password = binding.editTextPasswd.text.toString()

        sendLoginRequest(email, password)
    }

    private fun sendGetMeRequestBusiness(token: String): Boolean {
        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterScreen2.URL_SETUP_USER)
            .build()
        val service = retrofit.create(MainAPI::class.java)
        var statusFila = AtomicBoolean()
        statusFila.set(false)

        runBlocking {
            withContext(Dispatchers.Default) {
                val response = service.getBusinessMe("Bearer $token")
                if (response.isSuccessful) {
                    // Convert raw JSON to pretty JSON using GSON library

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )
                    Log.d("TEST",prettyJson)
                    val parts = prettyJson
                        .replace("\n","")
                        .replace("{","")
                        .replace("}","")
                        .replace("\"","")
                        .replace(" ","+")
                        .replace("++","")

                    val map = parts.split(",").associate {
                        val(left, right) = it.split(":")
                        left to right
                    }.toMutableMap()

                    Log.d("TEST", map.toString())

                    Log.d("TEST", map.toString())
                    if(map["statusFila"]?.drop(1) == "true"){
                        statusFila.set(true)
                    }
                    business_name = map["nome"]?.drop(1)?.replace("+"," ").toString()
                    business_code = map["codigo"]?.drop(1).toString()
                    Log.d("DENTRO", statusFila.toString())
                } else {
                    //binding.textCodigoInvalido.visibility = View.VISIBLE
                }
            }
        }

        Log.d("FORA", statusFila.toString())
        return statusFila.toString().toBoolean()
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

                    if(role == "usuario"){
                        loginUser(token)
                    }else{
                        val status = sendGetMeRequestBusiness(token)
                        Log.d("TesteX",status.toString())
                        loginBusiness(token, status)
                    }

                } else {
                    binding.textLoginInvalido.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun emailFocusListener()
    {
        binding.editTextEmail.addTextChangedListener(object : TextWatcher {
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
                binding.inputFieldEmail.helperText = validEmail()
                enableButton()
            }
        })
    }

    private fun validEmail(): String?
    {
        if (binding.editTextEmail.text.toString() == "")
            return resources.getString(R.string.digite_email)
        else{
            val pattern: Pattern = Patterns.EMAIL_ADDRESS
            if (pattern.matcher(binding.editTextEmail.text.toString()).matches())
                return null
            return resources.getString(R.string.invalidEmailFormat)
        }

    }

    private fun passwdFocusListener()
    {
        binding.editTextPasswd.addTextChangedListener(object : TextWatcher {
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
                binding.inputFieldPassword.helperText = validPasswd()
                enableButton()
            }
        })
    }

    private fun validPasswd(): String?
    {
        if ( binding.editTextPasswd.text.toString() == "" )
            return resources.getString(R.string.digite_senha)
        return null
    }

    private fun enableButton(){
        binding.buttonEntrar.isEnabled =
            (binding.inputFieldEmail.helperText == null &&
                            binding.inputFieldPassword.helperText == null)
    }


    fun loginUser(token:String){
        val context = binding.root.context
        val new_intent = Intent(context, UserHome::class.java)

        new_intent.putExtra("token", token)
        finish()
        context.startActivity(new_intent)
    }

    fun loginBusiness(token: String, statusFila: Boolean){

        Log.d("TESTEstatus",statusFila.toString())

        if (statusFila){
            val context = binding.root.context
            val intent = Intent(context, ManageQueue::class.java)
            intent.putExtra("token", token)
            intent.putExtra("business_name", business_name)
            intent.putExtra("business_code", business_code)
            finish()
            context.startActivity(intent)
        }
        else{
            val context = binding.root.context
            val intent = Intent(context, EstabWithoutQueueScreen::class.java)
            intent.putExtra("token", token)
            intent.putExtra("business_name", business_name)
            intent.putExtra("business_code", business_code)
            finish()
            context.startActivity(intent)
        }


    }

    fun signUp(){
        val context = binding.root.context
        val intent = Intent(context, RegisterScreen1::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        finish()
        context.startActivity(intent)
    }
}