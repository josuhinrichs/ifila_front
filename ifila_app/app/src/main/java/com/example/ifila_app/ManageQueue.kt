package com.example.ifila_app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.ifila_app.databinding.ActivityManageQueueBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import retrofit2.Retrofit
import java.util.concurrent.atomic.AtomicReference

class ManageQueue : AppCompatActivity() {

    private lateinit var binding: ActivityManageQueueBinding
    lateinit var token:String
    lateinit var business_name : String
    lateinit var business_code: String
    var qtdUsuariosFilaPrincipal = AtomicReference<String>()
    var qtdUsuariosFilaPrioridade = AtomicReference<String>()

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

        goToCheckQueueInfo()


    }

    private fun goToCheckQueueInfo(){
        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterScreen2.URL_SETUP_USER)
            .build()
        val service = retrofit.create(MainAPI::class.java)

        runBlocking {
            // Do the POST request and get response
            val response = service.getBusiness(business_code,"Bearer $token")

            runBlocking {
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
                    Log.d("NUMERO", map.toString())
                    qtdUsuariosFilaPrincipal.set(map["qtdPessoasPrincipal"]?.drop(1)?.toInt().toString())
                    qtdUsuariosFilaPrioridade.set(map["qtdPessoasPrioridade"]?.drop(1).toString())
                    return@runBlocking
                } else {
                }
            }
        }
        binding.textTamFilaPrincipal.text =
            "Fila Padrão - ${qtdUsuariosFilaPrincipal.toString()} Pessoa(s)"
        binding.textTamFilaPrioritaria.text =
            "Fila Prioridade - ${qtdUsuariosFilaPrioridade.toString()} Pessoa(s)"
        return
    }

    private fun goToCallNextUser(){
        goToCheckQueueInfo()
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
                                ?.string()
                        )
                    )
                    Log.d("Pretty Printed JSON :", prettyJson)
                    binding.buttonChamar.isEnabled = false
                    binding.buttonAtender.isEnabled = true

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

                    val nome = map["nome"] ?: ""
                    val cpf = map["cpf"] ?: ""
                    val numero_celular = map["numeroCelular"] ?: ""


                    confirmPopUpCall(nome.drop(1).replace("+", " "), cpf.drop(1), numero_celular.drop(1))
                } else {
                    confirmPopUpCallErro()
                    Log.d("ERRORcall", token)
                }
            }
        }
    }

    fun goToAttendNextUser(){
        goToCheckQueueInfo()
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

                    val nome = map["nome"] ?: ""
                    val cpf = map["cpf"] ?: ""
                    val numero_celular = map["numeroCelular"] ?: ""


                    confirmPopUpAttend(nome.drop(1).replace("+", " "), cpf.drop(1), numero_celular.drop(1))

                } else {
                    Log.d("ERROR attend", token)
                }
            }
        }
    }

    fun goToSkipUser(){
        goToCheckQueueInfo()
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

                    val nome = map["nome"] ?: ""
                    val cpf = map["cpf"] ?: ""
                    val numero_celular = map["numeroCelular"] ?: ""


                    confirmPopUpSkip(nome.drop(1).replace("+", " "), cpf.drop(1), numero_celular.drop(1))

                } else {
                    confirmPopUpSkipErro()
                    Log.d("ERROR skip", token)
                }
            }
        }
    }

    fun goToEstabWithouQueue(){
        startCloseQueue()
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

    private fun confirmPopUpCall(nome: String, cpf: String, numero_celular: String){
        val builder = MaterialAlertDialogBuilder(binding.root.context)
        builder.setTitle("Cliente Chamado!")

        builder.setMessage("Nome do cliente: $nome\nCPF: $cpf\nNumero de Celular: $numero_celular")

        builder.setPositiveButton("Confirmar") { dialog, which ->

        }

        val dialog = builder.create()
        dialog.show()

    }

    private fun confirmPopUpCallErro(){
        val builder = MaterialAlertDialogBuilder(binding.root.context)
        builder.setTitle("Não foi possível chamar")

        builder.setMessage("Esta fila está vazia.")

        builder.setPositiveButton("OK") { dialog, which ->

        }

        val dialog = builder.create()
        dialog.show()

    }

    private fun confirmPopUpAttend(nome: String, cpf: String, numero_celular: String){
        val builder = MaterialAlertDialogBuilder(binding.root.context)
        builder.setTitle("Cliente Atendido!")

        builder.setMessage("Nome do cliente: $nome\nCPF: $cpf\nNumero de Celular: $numero_celular")

        builder.setPositiveButton("Confirmar") { dialog, which ->

        }

        val dialog = builder.create()
        dialog.show()

    }

    private fun confirmPopUpSkip(nome: String, cpf: String, numero_celular: String){
        val builder = MaterialAlertDialogBuilder(binding.root.context)
        builder.setTitle("Cliente Pulado!")

        builder.setMessage("Nome do cliente: $nome\nCPF: $cpf\nNumero de Celular: $numero_celular")

        builder.setPositiveButton("Confirmar") { dialog, which ->

        }

        val dialog = builder.create()
        dialog.show()

    }

    private fun confirmPopUpSkipErro(){
        val builder = MaterialAlertDialogBuilder(binding.root.context)
        builder.setTitle("Não foi possível pular")

        builder.setMessage("O cliente da vez ainda não foi chamado.")

        builder.setPositiveButton("OK") { dialog, which ->

        }

        val dialog = builder.create()
        dialog.show()

    }


}