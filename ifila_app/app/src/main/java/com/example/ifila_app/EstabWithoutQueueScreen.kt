package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ifila_app.databinding.ActivityEstabWithoutQueueScreenBinding
import com.example.ifila_app.databinding.ActivityMainBinding

class EstabWithoutQueueScreen : AppCompatActivity() {
    private lateinit var binding: ActivityEstabWithoutQueueScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstabWithoutQueueScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCriarFila.setOnClickListener { goToCreateQueue() }

    }

    private fun goToCreateQueue(){
        val context = binding.root.context
        val intent = Intent(context, CreateQueueScreen::class.java)
        context.startActivity(intent)
    }
}