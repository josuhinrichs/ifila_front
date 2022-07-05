package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ifila_app.databinding.ActivityEstabWithoutQueueScreenBinding
import com.example.ifila_app.databinding.ActivityMainBinding

class EstabWithoutQueueScreen : AppCompatActivity() {
    private lateinit var binding: ActivityEstabWithoutQueueScreenBinding
    lateinit var token:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstabWithoutQueueScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        token = intent.extras?.get("token").toString()

        binding.buttonCriarFila.setOnClickListener { goToCreateQueue() }

    }

    private fun goToCreateQueue(){
        val context = binding.root.context
        val intent = Intent(context, CreateQueueScreen::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("token", token)
        finish()
        context.startActivity(intent)

    }
}