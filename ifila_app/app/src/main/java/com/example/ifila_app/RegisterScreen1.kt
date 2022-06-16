package com.example.ifila_app

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.ifila_app.databinding.ActivityRegisterScreen1Binding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*


class RegisterScreen1 : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterScreen1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterScreen1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        nameFocusListener(binding)
        cpfFocusListener(binding)
        phoneFocusListener(binding)
        birthFocusListener(binding)

        binding.buttonContinuar.isEnabled = false

        binding.buttonContinuar.setOnClickListener{ goToRegister2(binding) }
        binding.buttonCancelar.setOnClickListener { cancel(binding) }
        binding.editTextPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())



        binding.editTextBirth.inputType = InputType.TYPE_NULL;
        binding.editTextBirth.setOnClickListener{ showDatePicker(binding) }
        binding.editTextBirth.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                showDatePicker(binding)
            }
        }

    }

    private fun goToRegister2(binding: ActivityRegisterScreen1Binding){
        val name = binding.editTextName.text.toString()
        val cpf = binding.editTextName.text.toString()
        val phoneNumber = binding.editTextPhone.toString()
        val birth = binding.editTextBirth.toString()

        if (binding.inputFieldName.helperText == null &&
            binding.inputFieldCpf.helperText == null &&
            binding.inputFieldPhone.helperText == null &&
            binding.inputFieldBirth.helperText == null)
        {
            val context = binding.root.context
            val intent = Intent(context, RegisterScreen2::class.java)

            intent.putExtra("name", name)
            intent.putExtra("cpf", cpf)
            intent.putExtra("phoneNumber", phoneNumber)
            intent.putExtra("birth", birth)

            context.startActivity(intent)
        } else { incompleteFields(binding) }
    }

    private fun cancel(binding: ActivityRegisterScreen1Binding){
        val context = binding.root.context
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }

    private fun incompleteFields(binding: ActivityRegisterScreen1Binding){
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle(resources.getString(R.string.incompleteFieldsTitle))
            .setIcon(R.drawable.ic_baseline_error_outline_24)
            .setMessage(resources.getString(R.string.incompleteFields))
            .setPositiveButton(resources.getString(R.string.samePsswdAccept)) { dialog, which ->
                // Respond to positive button press
            }
            .show()
    }

    private fun showDatePicker( binding: ActivityRegisterScreen1Binding ){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(binding.root.context,R.style.DialogTheme,
            { _: DatePicker, mYear, mMonth, mDay ->
                val newDay = if(day < 10) "0$mDay" else mDay.toString()
                val newMonth = if(mMonth < 10) "0${mMonth+1}" else (mMonth+1).toString()

                binding.editTextBirth.setText("$newDay/$newMonth/$mYear")
            }, year, month, day )
        datePicker.show()
    }

    private fun nameFocusListener( binding: ActivityRegisterScreen1Binding )
    {
        binding.editTextName.addTextChangedListener(object : TextWatcher {
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
                if(binding.editTextName.text.toString() == "")
                    binding.inputFieldName.helperText = resources.getString(R.string.required)
                else {
                    binding.inputFieldName.helperText = null
                }
                enableButton()
            }
        })
    }

    private fun cpfFocusListener( binding: ActivityRegisterScreen1Binding )
    {
        binding.editTextCpf.addTextChangedListener(object : TextWatcher {
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
                binding.inputFieldCpf.helperText = validCpf()
                enableButton()
            }
        })
    }

    private fun validCpf(): String?
    {
        if(binding.editTextCpf.text.toString() == "")
        {
            return resources.getString(R.string.required)
        }
        if (binding.editTextCpf.text.toString().length < 11)
            return resources.getString(R.string.typeValidCpf)
        return null
    }

    private fun phoneFocusListener( binding: ActivityRegisterScreen1Binding )
    {
        binding.editTextPhone.addTextChangedListener(object : TextWatcher {
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
                binding.inputFieldPhone.helperText = validPhone()
                enableButton()
            }
        })
    }

    private fun validPhone(): String?
    {
        val phoneText = binding.editTextPhone.text.toString()

        if(phoneText == "")
        {
            return resources.getString(R.string.required)
        }
        if (phoneText.length < 13)
            return resources.getString(R.string.typeValidNumber)
        if(!phoneText.matches(".*[0-9].*".toRegex()))
        {
            return resources.getString(R.string.typeOnlyDigits)
        }
        return null
    }

    private fun birthFocusListener( binding: ActivityRegisterScreen1Binding )
    {

        binding.editTextBirth.addTextChangedListener(object : TextWatcher {
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
                if(binding.editTextBirth.text.toString() == "")
                    binding.inputFieldBirth.helperText = resources.getString(R.string.required)
                else {
                    binding.inputFieldBirth.helperText = null
                }
                enableButton()
            }
        })
    }

    private fun enableButton(){
        binding.buttonContinuar.isEnabled =
            (
                binding.inputFieldName.helperText == null &&
                binding.inputFieldCpf.helperText == null &&
                binding.inputFieldPhone.helperText == null &&
                binding.inputFieldBirth.helperText == null)
    }

}

