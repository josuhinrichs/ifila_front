package com.example.ifila_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ifila_app.databinding.ActivityRegisterScreen1Binding

class RegisterScreen1 : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterScreen1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterScreen1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonBack.setOnClickListener{ finish() }
        binding.buttonContinuar.setOnClickListener{ goToRegister2(binding) }
        binding.buttonCancelar.setOnClickListener { cancel(binding) }
    }

    fun goToRegister2(binding: ActivityRegisterScreen1Binding){
        val context = binding.root.context
        val intent = Intent(context, RegisterScreen2::class.java)
        context.startActivity(intent)
    }

    fun cancel(binding: ActivityRegisterScreen1Binding){
        val context = binding.root.context
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }
}

