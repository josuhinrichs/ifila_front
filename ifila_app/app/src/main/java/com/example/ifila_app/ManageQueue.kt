package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ifila_app.databinding.ActivityManageQueueBinding

class ManageQueue : AppCompatActivity() {

    private lateinit var binding: ActivityManageQueueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_queue)

        binding.buttonFecharFila.setOnClickListener { goToEstabWithouQueue() }

    }

    fun goToEstabWithouQueue(){
        val context = binding.root.context
        val intent = Intent(context, EstabWithoutQueueScreen::class.java)//Coloquei pra ir pra tela de ir pra Estabelecimento sem fila
        context.startActivity(intent)
    }
}