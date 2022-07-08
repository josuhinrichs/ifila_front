package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.ifila_app.databinding.ActivityMainBinding
import com.example.ifila_app.databinding.ActivityRegisterScreen1Binding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegistrar.setOnClickListener{ goToRegister(binding) }
        binding.buttonEntrar.setOnClickListener { goToLogin() }
        binding.buttonConvidado.setOnClickListener { goToConvidado() }
    }

    private fun goToRegister(binding: ActivityMainBinding){
        val context = binding.root.context
        val intent = Intent(context, RegisterScreen1::class.java)
        context.startActivity(intent)
    }

    private fun goToLogin(){
        val context = binding.root.context
        val intent = Intent(context, LoginScreen::class.java)
        context.startActivity(intent)
    }

    fun goToConvidado(){
        val context = binding.root.context
        upComingPopUp()
        //val intent = Intent(context, EnterCodeScreen::class.java)//Coloquei pra ir pra tela de inserir código
        //context.startActivity(intent)
    }


    private fun upComingPopUp(){
        val builder = MaterialAlertDialogBuilder(binding.root.context)
        builder.setTitle("Função em desenvolvimento...")
        builder.setMessage("Sentimos muito pela incoveniência, estamos trabalhando duro para implementar " +
                "novas funcionalidades e enriquecer a sua experiência no i.fila.")


        builder.setPositiveButton("Ok") { dialog, which ->

        }


        val dialog = builder.create()
        dialog.show()

    }
}



