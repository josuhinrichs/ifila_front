package com.example.ifila_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ifila_app.databinding.ActivityRegisterScreen2Binding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RegisterScreen2 : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterScreen2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterScreen2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonContinuar.setOnClickListener{
            if (binding.editTextPasswd.text == binding.editTextConfirmPasswd.text )
                goToRegisterFinish(binding)
            else
                samePasswdAlert(binding)

        }
        binding.buttonCancelar.setOnClickListener { cancel(binding) }
    }

    private fun goToRegisterFinish(binding: ActivityRegisterScreen2Binding){
        val extras = intent.extras
        if (extras != null) {
            val name = extras.getString("name")
            val cpf = extras.getInt("cpf")
            val phoneNumber = extras.getInt("phoneNumber")
            val birth = extras.getString("birth")
            //The key argument here must match that used in the other activity
        }


        val context = binding.root.context
        val intent = Intent(context, RegisterScreenFinish::class.java)
        context.startActivity(intent)
    }

    private fun cancel(binding: ActivityRegisterScreen2Binding){
        val context = binding.root.context
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }

    private fun samePasswdAlert(binding: ActivityRegisterScreen2Binding){
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle(resources.getString(R.string.samePsswdTitle))
            .setIcon(R.drawable.ic_baseline_error_outline_24)
            .setMessage(resources.getString(R.string.samePsswdSupportText))
            .setPositiveButton(resources.getString(R.string.samePsswdAccept)) { dialog, which ->
                // Respond to positive button press
            }
            .show()
    }
}