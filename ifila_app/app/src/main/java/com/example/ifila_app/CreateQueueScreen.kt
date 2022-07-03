package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ifila_app.databinding.ActivityCreateQueueScreenBinding

class CreateQueueScreen : AppCompatActivity() {
    private lateinit var binding: ActivityCreateQueueScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_queue_screen)

        binding.buttonFinalizar.setOnClickListener { goToManageQueue() }
    }

    fun goToManageQueue(){
        val context = binding.root.context
        val intent = Intent(context, ManageQueue::class.java)
        context.startActivity(intent)
    }
}