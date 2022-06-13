package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ifila_app.databinding.ActivityRegisterScreen1Binding

class RegisterScreen1 : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterScreen1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterScreen1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonContinuar.setOnClickListener{ goToRegister2(binding) }
    }

    fun goToRegister2(binding: ActivityRegisterScreen1Binding){
        val context = binding.root.context
        val intent = Intent(context, RegisterScreen2::class.java)
        context.startActivity(intent)
    }
}

