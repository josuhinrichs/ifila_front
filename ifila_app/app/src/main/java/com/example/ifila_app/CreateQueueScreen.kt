package com.example.ifila_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.ifila_app.databinding.ActivityCreateQueueScreenBinding
import com.example.ifila_app.databinding.ActivityEstabWithoutQueueScreenBinding
import com.example.ifila_app.databinding.ActivityRegisterScreen1Binding
import com.google.android.material.timepicker.TimeFormat

class CreateQueueScreen : AppCompatActivity() {
    private lateinit var binding: ActivityCreateQueueScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateQueueScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonFinalizar.isEnabled = false

        //binding.editTextLimiteTempo.addTextChangedListener()

        limitPeopleFocusListener()
        limitTimeFocusListener()


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

    private fun limitPeopleFocusListener( )
    {
        binding.editTextLimitePessoas.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                enableButton()
            }
        })
    }

    private fun limitTimeFocusListener( )
    {
        binding.editTextLimiteTempo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                enableButton()
            }
        })
    }

    private fun enableButton() {
        binding.buttonFinalizar.isEnabled =
            (
                    binding.editTextLimitePessoas.text != null &&
                            binding.editTextLimiteTempo.text != null
                    )
    }


}