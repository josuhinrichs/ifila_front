package com.example.ifila_app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ifila_app.databinding.ActivityRegisterScreen1Binding
import com.example.ifila_app.databinding.FragmentUserCodeQueueBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "token"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserCodeQueueFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserCodeQueueFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var token: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentUserCodeQueueBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserCodeQueueBinding.inflate(layoutInflater)
        binding.buttonBuscarEst.setOnClickListener { startCodRequest()}
        // Inflate the layout for this fragment
        val view_view = inflater.inflate(R.layout.fragment_user_code_queue, container, false)
        val botao = view_view.findViewById<Button>(R.id.button_buscarEst)
        botao.setOnClickListener { startCodRequest()}
        token = arguments?.getString("token")
        codeFocusListener()
        return view_view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserCodeQueueFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserCodeQueueFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun startCodRequest(){
        token?.let { sendCodRequest(it.toString()) }
    }

    private fun sendCodRequest(token: String){

        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterScreen2.URL_SETUP_USER)
            .build()
        val service = retrofit.create(MainAPI::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val input_code = view?.findViewById<TextView>(R.id.editTextCode)?.text.toString()
            val response = service.getBusiness(input_code, "Bearer $token")

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    // Convert raw JSON to pretty JSON using GSON library

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )

                    val parts = prettyJson
                        .replace("\n","")
                        .replace("{","")
                        .replace("}","")
                        .replace("\"","")
                        .replace(" ","+")
                        .replace("++","")

                    val map = parts.split(",").associate {
                        val(left, right) = it.split(":")
                        left to right
                    }.toMutableMap()

                    val nome = map["nome"] ?: ""
                    val endereco = map["endereco"] ?: ""
                    val telefone = map["telefone"] ?: ""
                    val descricao = map["descricao"] ?: ""
                    val horarioAbertura = map["horarioAbertura"] ?: ""
                    val horarioFechamento = map["horarioFechamento"] ?: ""
                   // val dataDeCriacao = map["dataDeCriacao"] ?: ""
                    val statusFila = map["statusFila"] ?: ""
                    val categoria = map["categoria"] ?: ""
                    val link_imagem= map["linkImagem"] ?: ""
                    val code = map["codigo"] ?: ""

                    Log.d("TEST 1",prettyJson)

                    val fragment = UserQueueFragment()
                    val bundle = Bundle()
                    bundle.putString("token", token)
                    bundle.putString("codigo", code.drop(1))
                    bundle.putString("nome_estabelecimento", nome.drop(1).replace("+"," "))
                    bundle.putString("descricao", descricao.drop(1))
                    bundle.putString("fila_status", statusFila.drop(1))
                    bundle.putString("categoria", categoria.drop(1))
                    bundle.putString("link_imagem", link_imagem.drop(1))
                    fragment.arguments = bundle
                    Log.d("TEST 2", map.toString())
                    replaceFragment(fragment)

                } else {
                    view?.findViewById<TextView>(R.id.text_invalid_business_code)?.visibility = View.VISIBLE
                }
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    private fun replaceFragment( fragment: Fragment){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout_code_queue, fragment)
        fragmentTransaction.commit()
    }

    private fun codeFocusListener()
    {
        view?.findViewById<EditText>(R.id.editTextCode)?.addTextChangedListener(object : TextWatcher {
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

    private fun enableButton(){
        view?.findViewById<EditText>(R.id.editTextCode)?.isEnabled   =
            (view?.findViewById<EditText>(R.id.editTextCode)?.text.toString()  != "" )
    }
}