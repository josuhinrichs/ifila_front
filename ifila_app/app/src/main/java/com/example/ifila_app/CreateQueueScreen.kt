package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ifila_app.databinding.ActivityCreateQueueScreenBinding
import com.example.ifila_app.databinding.ActivityEstabWithoutQueueScreenBinding

class CreateQueueScreen : AppCompatActivity() {
    private lateinit var binding: ActivityCreateQueueScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateQueueScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonFinalizar.setOnClickListener { goToManageQueue() }
        binding.buttonCancelar.setOnClickListener { goToCreateQueue() }
    }

    fun goToManageQueue(){
        val context = binding.root.context
        val intent = Intent(context, ManageQueue::class.java)
        context.startActivity(intent)
    }

    private fun goToCreateQueue(){
        val context = binding.root.context
        val intent = Intent(context, EstabWithoutQueueScreen::class.java)
        context.startActivity(intent)
    }
}