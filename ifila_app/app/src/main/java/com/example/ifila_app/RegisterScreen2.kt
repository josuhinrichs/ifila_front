package com.example.ifila_app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.ifila_app.databinding.ActivityRegisterScreen2Binding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

val EMAIL_ADDRESS: Pattern = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)
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

    private fun sendForm(email: String, passwd: String, name: String, cpf: String, phoneNumber: String, birth: String){
        val jsonObject = JSONObject()
        try {
            jsonObject.put("nome", name)
            jsonObject.put("dataDeNascimento", birth)
            jsonObject.put("senha", passwd)
            jsonObject.put("email", email)
            jsonObject.put("numeroCelular", phoneNumber)
            jsonObject.put("cpf", cpf)
        } catch (e: JSONException) {
            e.printStackTrace()
            goToFailed()
        }
        setupNewUser(jsonObject)
    }

    private fun setupNewUser (jsonObject: JSONObject) {
        val client = OkHttpClient()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonObject.toString().toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .url("http://ifila.com.br:8000/usuarios?role=user")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                print("COCOCOOCOPSKAOKSOAKOSKAO")
                goToFailed()
            }

            override fun onResponse(call: Call, response: Response) {
                print("COCOCOOCOPSKAOKSOAKOSKAO")
                when(response.code) {
                    201 -> goToSuccess()
                    200 -> goToSuccess()
                    else -> goToFailed()
                }
            }
        })
    }

    private fun goToRegisterFinish(binding: ActivityRegisterScreen2Binding){

        val email = binding.editTextEmail.text.toString()
        val passwd = binding.editTextPasswd.text.toString()

        if (binding.inputFieldEmail.helperText == null &&
            binding.inputFieldPassword.helperText == null &&
            binding.inputFieldPassword2.helperText == null)
        {
            val context = binding.root.context
            val lastIntent = intent
            val extras = lastIntent.extras
            val name = extras?.get("name").toString()
//            val name = extras?.getExtraString("name").toString()
            val cpf = extras?.get("cpf").toString()
            val phoneNumber = extras?.get("phoneNumber").toString()
            val birth = extras?.get("birth").toString()
            val birthFormatted = formatDate(birth,"dd/mm/yyyy", "yyyy-mm-dd" )
//
//            intent.putExtra("name", name)
//            intent.putExtra("cpf", cpf)
//            intent.putExtra("phoneNumber", phoneNumber)
//            intent.putExtra("birth", birth)
//
//            intent.putExtra("email", email)
//            intent.putExtra("password", passwd)
            sendForm(email, passwd, name, cpf, phoneNumber, birthFormatted)
        } else { incompleteFields(binding) }
    }

    private fun goToFailed(){
        val context = binding.root.context
        val intent = Intent(context, RegisterScreenFailed::class.java)
        context.startActivity(intent)
    }

    private fun goToSuccess(){
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
            else
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

    @Throws(ParseException::class)
    fun formatDate(
        date: String,
        initDateFormat: String,
        endDateFormat: String
    ): String {
        val initDate: Date = SimpleDateFormat(initDateFormat).parse(date)
        val formatter = SimpleDateFormat(endDateFormat)
        return formatter.format(initDate)
    }
}