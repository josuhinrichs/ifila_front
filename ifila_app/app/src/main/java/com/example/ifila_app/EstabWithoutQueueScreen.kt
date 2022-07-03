package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ifila_app.databinding.ActivityEstabWithoutQueueScreenBinding

class EstabWithoutQueueScreen : AppCompatActivity() {
    private lateinit var binding: ActivityEstabWithoutQueueScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estab_without_queue_screen)

        binding.buttonCriarFila.setOnClickListener { goToCreateQueue() }

    }

    private fun goToCreateQueue(){
        val context = binding.root.context
        val intent = Intent(context, CreateQueueScreen::class.java)
        context.startActivity(intent)
    }
}