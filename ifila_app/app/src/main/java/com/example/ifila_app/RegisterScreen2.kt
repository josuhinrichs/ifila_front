package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ifila_app.databinding.ActivityMainBinding
import com.example.ifila_app.databinding.ActivityRegisterScreen1Binding
import com.example.ifila_app.databinding.ActivityRegisterScreen2Binding

class RegisterScreen2 : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterScreen2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterScreen2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonBack.setOnClickListener{ finish() }
        binding.buttonContinuar.setOnClickListener{ goToRegisterFinish(binding) }
        binding.buttonCancelar.setOnClickListener { cancel(binding) }
    }

    private fun goToRegisterFinish(binding: ActivityRegisterScreen2Binding){
        val context = binding.root.context
        val intent = Intent(context, RegisterScreenFinish::class.java)
        context.startActivity(intent)
    }

    private fun cancel(binding: ActivityRegisterScreen2Binding){
        val context = binding.root.context
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }
}