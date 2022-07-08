package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ifila_app.databinding.ActivityMainBinding
import com.example.ifila_app.databinding.ActivityRegisterScreenFailedBinding

class RegisterScreenFailed : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterScreenFailedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterScreenFailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRetornar.setOnClickListener { goToMain() }
    }

    private fun goToMain(){
        val context = binding.root.context
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }
}