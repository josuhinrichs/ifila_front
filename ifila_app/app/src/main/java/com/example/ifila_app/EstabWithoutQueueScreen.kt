package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ifila_app.databinding.ActivityEstabWithoutQueueScreenBinding
import com.example.ifila_app.databinding.ActivityMainBinding

class EstabWithoutQueueScreen : AppCompatActivity() {
    private lateinit var binding: ActivityEstabWithoutQueueScreenBinding
    lateinit var token:String
    var business_name:String? = null
    var business_code:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstabWithoutQueueScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        token = intent.extras?.get("token").toString()
        business_name = intent.extras?.get("business_name") as String?
        business_code = intent.extras?.get("business_code") as String?

        binding.buttonCriarFila.setOnClickListener { goToCreateQueue() }
        binding.textBusinessName2.text = business_name

    }

    private fun goToCreateQueue(){
        val context = binding.root.context
        val intent = Intent(context, CreateQueueScreen::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("business_name", business_name)
        intent.putExtra("business_code", business_code)
        intent.putExtra("token", token)
        finish()
        context.startActivity(intent)
    }
}