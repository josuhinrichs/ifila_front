package com.example.ifila_app

import android.os.Bundle
import android.util.AtomicFile
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.ifila_app.databinding.FragmentQueuePositionBinding
import com.example.ifila_app.databinding.FragmentUserSettingsBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.util.concurrent.atomic.AtomicReference

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserSettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserSettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var TOKEN: String = ""
    private var USER_NAME = AtomicReference<String>()
    private var USER_CPF = AtomicReference<String>()
    private var USER_EMAIL = AtomicReference<String>()
    private var USER_PHONE = AtomicReference<String>()
    private var USER_BIRTH = AtomicReference<String>()
//    private var USER_ADDRESS = AtomicReference<String>()
    private lateinit var binding: FragmentUserSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserSettingsBinding.inflate(layoutInflater)

        val view_view =  inflater.inflate(R.layout.fragment_user_settings, container, false)

        TOKEN = arguments?.getString("token").toString()
        getUserInfo()
        view_view.findViewById<TextView>(R.id.text_user_profile_name).text = USER_NAME.toString()
        view_view.findViewById<TextView>(R.id.text_user_profile_email).text = USER_EMAIL.toString()
        view_view.findViewById<TextView>(R.id.text_user_profile_phone).text = USER_PHONE.toString()
        view_view.findViewById<TextView>(R.id.text_user_profile_cpf).text = USER_CPF.toString()
        view_view.findViewById<TextView>(R.id.text_user_profile_birth).text = USER_BIRTH.toString()
//        view_view.findViewById<TextView>(R.id.text_user_profile_phone).text = USER_ADRESS.toString()
        //view_view.findViewById<Button>(R.id.button_profile_leave_account).setOnClickListener { parentFragmentManager.onBack }
        return view_view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserSettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserSettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun getUserInfo() {
        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterScreen2.URL_SETUP_USER)
            .build()
        val service = retrofit.create(MainAPI::class.java)
        runBlocking {
            val response = service.getUserMe("Bearer $TOKEN")
            withContext(Dispatchers.Default) {

                if (response.isSuccessful) {
                    // Convert raw JSON to pretty JSON using GSON library

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )
                    Log.d("TEST", prettyJson)
                    val parts = prettyJson
                        .replace("\n", "")
                        .replace("{", "")
                        .replace("}", "")
                        .replace("\"", "")
                        .replace(" ", "+")
                        .replace("++", "")

                    val map = parts.split(",").associate {
                        val (left, right) = it.split(":")
                        left to right
                    }.toMutableMap()

                    Log.d("TEST", map.toString())
                    USER_NAME.set(
                        map["nome"]?.drop(1)?.replace("+", " ").toString()
                    )
                    USER_EMAIL.set(
                        map["email"]?.drop(1)?.replace("+", " ").toString()
                    )
                    USER_CPF.set(
                        map["cpf"]?.drop(1)?.replace("+", " ").toString()
                    )
                    USER_PHONE.set(
                        map["numeroCelular"]?.drop(1)?.replace("+", " ").toString()
                    )
                    USER_BIRTH.set(
                        map["dataDeNascimento"]?.drop(1)?.replace("+", " ").toString()
                    )
/*                    USER_ADDRESS.set(
                        map["endereco"]?.drop(1)?.replace("+", " ").toString()
                    )*/
                } else {
                    //binding.textCodigoInvalido.visibility = View.VISIBLE
                }
            }
        }
    }
}