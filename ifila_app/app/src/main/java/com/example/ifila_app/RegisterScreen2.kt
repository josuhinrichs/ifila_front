package com.example.ifila_app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.ifila_app.databinding.ActivityRegisterScreen2Binding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.regex.Pattern


class RegisterScreen2 : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterScreen2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterScreen2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        emailFocusListener(binding)
        passwdFocusListener(binding)
        confirmPasswdFocusListener(binding)

        binding.buttonContinuar.isEnabled = false
        binding.buttonContinuar.setOnClickListener { goToRegisterFinish(binding) }
        binding.buttonCancelar.setOnClickListener { cancel(binding) }
    }

    private fun goToRegisterFinish(binding: ActivityRegisterScreen2Binding){

        val email = binding.editTextEmail.text.toString()
        val passwd = binding.editTextPasswd.text.toString()

        if (binding.inputFieldEmail.helperText == null &&
            binding.inputFieldPassword.helperText == null &&
            binding.inputFieldPassword2.helperText == null)
        {
            val context = binding.root.context
            val intent = Intent(context, RegisterScreenFinish::class.java)

            val extras = intent.extras
            val name = extras?.getString("name").toString()
            val cpf = extras?.getInt("cpf").toString()
            val phoneNumber = extras?.getInt("phoneNumber").toString()
            val birth = extras?.getString("birth").toString()

            intent.putExtra("name", name)
            intent.putExtra("cpf", cpf)
            intent.putExtra("phoneNumber", phoneNumber)
            intent.putExtra("birth", birth)

            intent.putExtra("email", email)
            intent.putExtra("password", passwd)

            context.startActivity(intent)
        } else { incompleteFields(binding) }
    }

    private fun cancel(binding: ActivityRegisterScreen2Binding){
        val context = binding.root.context
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }

//    private fun samePasswdAlert(binding: ActivityRegisterScreen2Binding){
//        MaterialAlertDialogBuilder(binding.root.context)
//            .setTitle(resources.getString(R.string.samePsswdTitle))
//            .setIcon(R.drawable.ic_baseline_error_outline_24)
//            .setMessage(resources.getString(R.string.samePsswdSupportText))
//            .setPositiveButton(resources.getString(R.string.samePsswdAccept)) { dialog, which ->
//                // Respond to positive button press
//            }
//            .show()
//    }

    private fun incompleteFields(binding: ActivityRegisterScreen2Binding){
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle(resources.getString(R.string.incompleteFieldsTitle))
            .setIcon(R.drawable.ic_baseline_error_outline_24)
            .setMessage(resources.getString(R.string.incompleteFields))
            .setPositiveButton(resources.getString(R.string.samePsswdAccept)) { dialog, which ->
                // Respond to positive button press
            }
            .show()
    }

    private fun emailFocusListener( binding: ActivityRegisterScreen2Binding)
    {
        binding.editTextEmail.addTextChangedListener(object : TextWatcher {
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
                binding.inputFieldEmail.helperText = validEmail()
                enableButton()
            }
        })
    }

    private fun validEmail(): String?
    {
        if (binding.editTextEmail.text.toString() == "")
            return resources.getString(R.string.required)
        else{
            val pattern: Pattern = Patterns.EMAIL_ADDRESS
            if (pattern.matcher(binding.editTextEmail.text.toString()).matches())
                return null
            return resources.getString(R.string.invalidEmailFormat)
        }

    }

    private fun passwdFocusListener( binding: ActivityRegisterScreen2Binding )
    {
        binding.editTextPasswd.addTextChangedListener(object : TextWatcher {
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
                binding.inputFieldPassword.helperText = validPasswd()
                enableButton()
            }
        })
    }

    private fun validPasswd(): String?
    {
        if ( binding.editTextPasswd.text.toString() == "" )
            return resources.getString(R.string.required)
        if (binding.editTextPasswd.text.toString().length < 6)
            return resources.getString(R.string.minPasswd)
        if ( !binding.editTextPasswd.text.toString().contains( "[0-9]".toRegex() ) )
            return resources.getString(R.string.minPasswd)
        return null
    }

    private fun confirmPasswdFocusListener( binding: ActivityRegisterScreen2Binding )
    {
        binding.editTextConfirmPasswd.addTextChangedListener(object : TextWatcher {
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
                binding.inputFieldPassword2.helperText = validConfirmPasswd()
                enableButton()
            }
        })
    }

    private fun validConfirmPasswd(): String?
    {
        if ( binding.editTextConfirmPasswd.text.toString() == "" )
            return resources.getString(R.string.required)
        if ( binding.editTextPasswd.text.toString() != binding.editTextConfirmPasswd.text.toString() )
            return resources.getString(R.string.samePsswdSupportText)
        return null
    }

    private fun enableButton(){
        binding.buttonContinuar.isEnabled =
            (
                binding.inputFieldEmail.helperText == null &&
                        binding.inputFieldPassword.helperText == null &&
                        binding.inputFieldPassword2.helperText == null)
    }
}