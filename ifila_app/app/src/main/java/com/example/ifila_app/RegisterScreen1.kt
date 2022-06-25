package com.example.ifila_app

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
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

        binding.buttonContinuar2.isEnabled = false

        binding.buttonContinuar2.setOnClickListener{ goToRegister2(binding) }
        binding.buttonCancelar2.setOnClickListener { cancel(binding) }
        binding.editTextPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        val cpfMask = Mask.insert(Mask.CPF_MASK, binding.editTextCpf)
        binding.editTextCpf.addTextChangedListener( cpfMask )


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

                if (binding.editTextCpf.text.toString().length == 3){
                    binding.editTextCpf.text?.append("-")
                }
            }
        })
    }

    private fun validCpf(): String?
    {
        if(binding.editTextCpf.text.toString() == "")
        {
            return resources.getString(R.string.required)
        }
        if (binding.editTextCpf.text.toString().length < 14)
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
        binding.buttonContinuar2.isEnabled =
            (
                binding.inputFieldName.helperText == null &&
                binding.inputFieldCpf.helperText == null &&
                binding.inputFieldPhone.helperText == null &&
                binding.inputFieldBirth.helperText == null)
    }

}

object Mask {
    var CPF_MASK = "###.###.###-##"
    var CELULAR_MASK = "(##) #### #####"
    var CEP_MASK = "#####-###"
    fun unmask(s: String): String {
        return s.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
            .replace("[/]".toRegex(), "").replace("[(]".toRegex(), "")
            .replace("[)]".toRegex(), "").replace(" ".toRegex(), "")
            .replace(",".toRegex(), "")
    }

    fun isASign(c: Char): Boolean {
        return c == '.' || c == '-' || c == '/' || c == '(' || c == ')' || c == ',' || c == ' '
    }

    fun mask(mask: String, text: String): String {
        var i = 0
        var mascara = ""
        for (m in mask.toCharArray()) {
            if (m != '#') {
                mascara += m
                continue
            }
            mascara += try {
                text[i]
            } catch (e: Exception) {
                break
            }
            i++
        }
        return mascara
    }

    fun insert(mask: String, ediTxt: EditText): TextWatcher {
        return object : TextWatcher {
            var isUpdating = false
            var old = ""
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val str = unmask(s.toString())
                var mascara = ""
                if (isUpdating) {
                    old = str
                    isUpdating = false
                    return
                }
                var index = 0
                for (i in 0 until mask.length) {
                    val m = mask[i]
                    if (m != '#') {
                        if (index == str.length && str.length < old.length) {
                            continue
                        }
                        mascara += m
                        continue
                    }
                    mascara += try {
                        str[index]
                    } catch (e: Exception) {
                        break
                    }
                    index++
                }
                if (mascara.isNotEmpty()) {
                    var last_char = mascara[mascara.length - 1]
                    var hadSign = false
                    while (isASign(last_char) && str.length == old.length) {
                        mascara = mascara.substring(0, mascara.length - 1)
                        last_char = mascara[mascara.length - 1]
                        hadSign = true
                    }
                    if (mascara.isNotEmpty() && hadSign) {
                        mascara = mascara.substring(0, mascara.length - 1)
                    }
                }
                isUpdating = true
                ediTxt.setText(mascara)
                ediTxt.setSelection(mascara.length)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        }
    }
}

