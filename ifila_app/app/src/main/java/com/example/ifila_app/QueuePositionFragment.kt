package com.example.ifila_app

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.ifila_app.databinding.FragmentQueuePositionBinding
import com.example.ifila_app.databinding.FragmentUserQueueBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import retrofit2.Retrofit
import java.util.concurrent.atomic.AtomicBoolean

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QueuePositionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QueuePositionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var TOKEN: String? = null
    private var BUSINESS_NAME: String=""
    private var CODE: String? = null
    private lateinit var binding: FragmentQueuePositionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQueuePositionBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        val view_view =  inflater.inflate(R.layout.fragment_queue_position, container, false)

        TOKEN = arguments?.getString("token")
        BUSINESS_NAME = arguments?.getString("nomeEstabelecimento").toString()
        Log.d("NOME ESTABELECIMENTO", BUSINESS_NAME)
        CODE = arguments?.getString("codigo")

        val botao_confirmar_presença = view_view.findViewById<Button>(R.id.button_confirmar_presenca)
        botao_confirmar_presença.isEnabled = false

        view_view.findViewById<Button>(R.id.button_sair_fila2).setOnClickListener { leaveQueue() }
        view_view.findViewById<TextView>(R.id.nome_estabelecimento2).text = BUSINESS_NAME
        botao_confirmar_presença.setOnClickListener { goToConfirmPresence() }
        view_view.findViewById<Button>(R.id.button_verificar_posicao).setOnClickListener { goToVerifyPosition() }
        return view_view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QueuePositionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QueuePositionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun leaveQueue(){
        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterScreen2.URL_SETUP_USER)
            .build()
        val service = retrofit.create(MainAPI::class.java)

        runBlocking {
            withContext(Dispatchers.Default) {
                val response = service.leaveQueue("Bearer $TOKEN")
                if (response.isSuccessful) {

                    Log.d("TEST","SAIU DA FILA")
                } else {
                    //binding.textCodigoInvalido.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun goToConfirmPresence(){
        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterScreen2.URL_SETUP_USER)
            .build()
        val service = retrofit.create(MainAPI::class.java)
        val success:AtomicBoolean = AtomicBoolean()

        runBlocking {
            val response = service.confirmPresence("Bearer $TOKEN")
            withContext(Dispatchers.Default) {
                if (response.isSuccessful) {
                    // Convert raw JSON to pretty JSON using GSON library

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )
                    success.set(true)

                    Log.d("TEST PRESENCA",prettyJson)

                } else {
                    //confirmPopUpConfirmErro()
                    Log.d("TEST PRESENCA","erro - estabelecimento não chamou")
                    success.set(false)
//                  binding.textCodigoInvalido.visibility = View.VISIBLE
                }
            }
        }
        if(success.toString().toBoolean())
            confirmPopUpConfirm()
        else
            confirmPopUpConfirmErro()
    }

    private fun goToVerifyPosition(){
        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterScreen2.URL_SETUP_USER)
            .build()
        val service = retrofit.create(MainAPI::class.java)
        val success = AtomicBoolean()
        var posicao = ""
        var deveConfirmarPresenca = ""
        runBlocking {
            val response = service.checkPositionInQueue("Bearer $TOKEN")
            withContext(Dispatchers.Default) {
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
                    Log.d(" Skip Pretty Printed MAAAAAP :", map.toString())

                    posicao = map["posicao"]?.drop(1).toString()
                    deveConfirmarPresenca = map["deveConfirmarPresenca"]?.drop(1).toString()

                    success.set(true)

                    Log.d("TEST PRESENCA",prettyJson)

                } else {
                    success.set(false)
                }
            }
        }
        if(success.toString().toBoolean())
            verifyPositionPopUp(posicao, deveConfirmarPresenca.toBoolean())
        else
            Log.d("TEST PRESENCA","Erro")
    }

    private fun confirmPopUpConfirm() {
        val builder = view?.let { MaterialAlertDialogBuilder(it.context) }
        if (builder != null) {
            builder.setTitle("Presença Confirmada")
            view?.findViewById<Button>(R.id.button_verificar_posicao)?.isEnabled = false
            builder.setMessage("Dirija-se ao local de atendimento, você já será atendido.")

            builder.setPositiveButton("OK") { dialog, which ->

            }

            val dialog = builder.create()
            dialog.show()
        }

    }

    private fun verifyPositionPopUp(position:String, confirm:Boolean) {
        val builder = view?.let { MaterialAlertDialogBuilder(it.context) }
        if (builder != null) {
            builder.setTitle("Verificar Posição")
            if(confirm) {
                view?.findViewById<Button>(R.id.button_confirmar_presenca)?.isEnabled = true
                view?.findViewById<ImageView>(R.id.image_person_in_queue)?.setImageDrawable(resources.getDrawable(R.drawable.person_filled))
                builder.setMessage("É a sua vez! Confirme sua presença para notificar o estabelecimento que você está aqui.")
            }else
                builder.setMessage("Sua posição é: $position")

            builder.setPositiveButton("OK") { dialog, which ->
            }

            val dialog = builder.create()
            dialog.show()
        }

    }

    private fun confirmPopUpConfirmErro(){
        val builder = MaterialAlertDialogBuilder(binding.root.context)
        builder.setTitle("Aguarde um momento")

        builder.setMessage("Ainda não é sua vez. Em breve você será chamado!")

        builder.setPositiveButton("OK") { dialog, which ->

        }

        val dialog = builder.create()
        dialog.show()

    }


}