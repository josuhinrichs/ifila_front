package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ifila_app.databinding.ActivityEnterCodeScreenBinding
import com.example.ifila_app.databinding.ActivityRegisterScreen2Binding

class EnterCodeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityEnterCodeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterCodeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}