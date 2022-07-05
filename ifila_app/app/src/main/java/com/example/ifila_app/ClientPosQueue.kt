package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ifila_app.databinding.ActivityClientPosQueueBinding
import com.example.ifila_app.databinding.ActivityManageQueueBinding

class ClientPosQueue : AppCompatActivity() {
    lateinit var binding: ActivityClientPosQueueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientPosQueueBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.buttonSairFila.setOnClickListener { goToEnteCode() }
    }

//    private fun goToEnterCode(){
//        val context = binding.root.context
//        //val intent = Intent(context, EnterCodeScreen::class.java)//Coloquei pra ir pra tela de ir pra Estabelecimento sem fila
//        context.startActivity(intent)
//    }
}