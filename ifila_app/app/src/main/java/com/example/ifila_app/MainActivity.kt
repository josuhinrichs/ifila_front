package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ifila_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegistrar.setOnClickListener{ goToRegister(binding) }
        binding.buttonEntrar.setOnClickListener{ goToLogin(binding) }
    }

    private fun goToRegister(binding: ActivityMainBinding){
        val context = binding.root.context
        val intent = Intent(context, RegisterScreen1::class.java)
        context.startActivity(intent)
    }

    private fun goToLogin(binding: ActivityMainBinding){
        val context = binding.root.context
        val intent = Intent(context, LoginScreen::class.java)
        context.startActivity(intent)
    }


    fun goToRegisterFailed(binding: ActivityMainBinding){
        val context = binding.root.context
        val intent = Intent(context, RegisterScreenFailed::class.java)
        context.startActivity(intent)
    }
}

