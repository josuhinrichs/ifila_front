package com.example.ifila_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ifila_app.databinding.ActivityRegisterScreen1Binding
import com.example.ifila_app.databinding.ActivityUserHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class UserHome : AppCompatActivity() {
    private lateinit var binding: ActivityUserHomeBinding
    var bundle = Bundle()
    var BUSINESS_NAME = AtomicReference<String>()
    val role = "user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)
        val token = intent.extras?.get("token").toString()

        binding = ActivityUserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bundle.putString("token", token)
        val starter_fragment = UserHomeFragment()
        starter_fragment.arguments = bundle
        replaceFragment( starter_fragment )

        binding.bottomNavigationView.setOnItemSelectedListener{

            when(it.itemId){
                R.id.nav_home -> {
                    val fragment = UserHomeFragment()
                    bundle.putString("token", token)
                    fragment.arguments = bundle
                    replaceFragment( fragment )}
                R.id.nav_queue -> {
                    if (!checkIfQueue(token)) {
                        val fragment = UserCodeQueueFragment()
                        bundle.putString("token", token)
                        fragment.arguments = bundle
                        replaceFragment(fragment)
                    }else{
                        val fragment = QueuePositionFragment()
                        bundle.putString("nome_estabelecimento", BUSINESS_NAME.toString())
                        bundle.putString("token", token)
                        fragment.arguments = bundle
                        replaceFragment(fragment)
                    }
                }
                R.id.nav_profile -> {
                    val fragment = UserSettingsFragment()
                    bundle.putString("token", token)
                    fragment.arguments = bundle
                    replaceFragment( fragment )}
                else -> true
            }
            true
        }
    }

    override fun onBackPressed() {
        val builder = MaterialAlertDialogBuilder(binding.root.context)
        builder.setTitle("Sair")

        builder.setMessage("Voc?? deseja realmente sair?")

        builder.setPositiveButton("Confirmar") { dialog, which ->
            super.onBackPressed()
        }

        builder.setNegativeButton("Cancelar", null)

        val dialog = builder.create()
        dialog.show()
    }

    private fun replaceFragment( fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    private fun checkIfQueue(token:String): Boolean {
        return sendGetMeRequestUser(token)
    }

    private fun sendGetMeRequestUser(token: String): Boolean {
        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterScreen2.URL_SETUP_USER)
            .build()
        val service = retrofit.create(MainAPI::class.java)

        var inQueue = AtomicBoolean()
        inQueue.set(false)

        runBlocking {
            val response = service.getUserMe("Bearer $token")
            withContext(Dispatchers.Default) {

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
                    if(map["emFila"]?.drop(1) == "true"){
                        BUSINESS_NAME.set(map["nomeEstabelecimento"]?.drop(1)?.replace("+"," ").toString())
                        inQueue.set(true)
                    }
                    Log.d("TEST", map.toString())
                    Log.d("DENTRO", inQueue.toString())
                } else {
                    //binding.textCodigoInvalido.visibility = View.VISIBLE
                }
            }
        }

        Log.d("FORA", inQueue.toString())
        return inQueue.toString().toBoolean()
    }
}