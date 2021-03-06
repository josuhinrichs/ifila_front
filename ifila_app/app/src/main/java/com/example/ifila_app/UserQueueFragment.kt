package com.example.ifila_app

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.ifila_app.databinding.FragmentUserQueueBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit


private const val ARG_PARAM1 = "token"
private const val ARG_PARAM2 = "param2"

class UserQueueFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var TOKEN: String? = null
    private var BUSINESS_NAME: String? = null
    private var DESCRIPTION: String? = null
    private var IMAGE_LINK: String? = null
    private var QUEUE_STATUS: String? = null
    private var BUSINESS_TYPE: String? = null
    private var CODE: String? = null
    private lateinit var binding: FragmentUserQueueBinding


    private var QUEUE_SIZE_PRINCIPAL: String? = null
    private var QUEUE_SIZE_PRIORITY: String? = null
    private var QUEUE_TIME_PRINCIPAL: String? = null
    private var QUEUE_TIME_PRIORITY: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserQueueBinding.inflate(layoutInflater)
        //binding.buttonBuscarEst.setOnClickListener { startCodRequest()}

        BUSINESS_NAME = arguments?.getString("nome_estabelecimento")

        val view_view = inflater.inflate(R.layout.fragment_user_queue, container, false)
        val business_name = view_view.findViewById<TextView>(R.id.text_business_name)
        business_name.text = BUSINESS_NAME
        TOKEN = arguments?.getString("token")

        QUEUE_STATUS = arguments?.getString("fila_status")
        DESCRIPTION = arguments?.getString("descricao")
        IMAGE_LINK= arguments?.getString("link_imagem")
        IMAGE_LINK = "https://$IMAGE_LINK"
        CODE= arguments?.getString("codigo")
        BUSINESS_TYPE= arguments?.getString("categoria")
        QUEUE_SIZE_PRINCIPAL = arguments?.getString("qtdPessoasPrincipal")
        QUEUE_SIZE_PRIORITY = arguments?.getString("qtdPessoasPrioridade")
        QUEUE_TIME_PRINCIPAL = arguments?.getString("tempoMedioPrincipal")?.drop(3)
        val queue_time_principal_min = QUEUE_TIME_PRINCIPAL?.substring(0,2)+ "min"
        val queue_time_principal_second = QUEUE_TIME_PRINCIPAL?.substring(3) + "s"

        QUEUE_TIME_PRIORITY = arguments?.getString("tempoMedioPrioridade")?.drop(3)
        val queue_time_priority_min = QUEUE_TIME_PRIORITY?.substring(0,2)+ "min"
        val queue_time_priority_second = QUEUE_TIME_PRIORITY?.substring(3) + "s"

        val business_status = view_view.findViewById<TextView>(R.id.text_business_status)
        val business_size_principal = view_view.findViewById<TextView>(R.id.text_queue_size_principal)
        val business_size_priority = view_view.findViewById<TextView>(R.id.text_queue_size_priority)

        val wait_time_principal = view_view.findViewById<TextView>(R.id.text_avg_time_padrao)
        val wait_time_prioridade = view_view.findViewById<TextView>(R.id.text_avg_time_prioridade)

        if(QUEUE_STATUS.toBoolean()){
            business_status.text = "Aberto"
            view_view.findViewById<ConstraintLayout>(R.id.layout_queue_panel).visibility = View.VISIBLE
            business_size_principal.text = "$QUEUE_SIZE_PRINCIPAL Pessoa(s)"
            business_size_priority.text = "$QUEUE_SIZE_PRIORITY Pessoa(s)"
            wait_time_principal.text = "Espera estimada - $queue_time_principal_min $queue_time_principal_second"
            wait_time_prioridade.text = "Espera estimada - $queue_time_priority_min $queue_time_priority_second"
        }else {
            business_status.text = "Fechado"
            business_status.setTextColor(resources.getColor(R.color.red_main))
            view_view.findViewById<ConstraintLayout>(R.id.layout_queue_panel).visibility = View.INVISIBLE
        }

        view_view.findViewById<TextView>(R.id.text_business_type).text = BUSINESS_TYPE
        view_view.findViewById<TextView>(R.id.button_enter_queue).setOnClickListener{ confirmPopUp() }

        val image_view = view_view.findViewById<ImageView>(R.id.image_business_to_link)
        Picasso.get().load(IMAGE_LINK).into(image_view)
        BUSINESS_NAME?.let { Log.d("TESTE", it) }
        return view_view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserQueueFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserQueueFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun confirmPopUp(){
        val builder = MaterialAlertDialogBuilder(binding.root.context)
        builder.setTitle("Confirmar entrada na fila?")
        var priority = false

        val confirmBox = arrayOf("Entrar como grupo priorit??rio")
        val checkedBox = booleanArrayOf(false)
        builder.setMultiChoiceItems(confirmBox, checkedBox) { dialog, which, isChecked ->
            priority = true
        }
        builder.setPositiveButton("Confirmar") { dialog, which ->
            enterQueue(priority)
        }

        builder.setNegativeButton("Cancelar", null)

        val dialog = builder.create()
        dialog.show()

    }

    fun enterQueue(priority:Boolean){
        val retrofit = Retrofit.Builder()
            .baseUrl(RegisterScreen2.URL_SETUP_USER)
            .build()
        val service = retrofit.create(MainAPI::class.java)

        Log.d("ANTES", CODE.toString())
        Log.d("ANTES", TOKEN.toString())

        val priority_query = if(priority) "prioridade" else "principal"

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.enterQueue(CODE.toString(), priority_query,"Bearer $TOKEN")

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    // Convert raw JSON to pretty JSON using GSON library

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )

                    Log.d("TEST 1",prettyJson)

                    val fragment = QueuePositionFragment()
                    val bundle = Bundle()
                    bundle.putString("token", TOKEN)
                    bundle.putString("nome_estabelecimento", BUSINESS_NAME)
                    fragment.arguments = bundle
                    replaceFragment(fragment)

                } else {
                    Log.d("TEST","ERRO ERRO ")
                }
            }
        }
    }

    private fun replaceFragment( fragment: Fragment){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout_view_business, fragment)
        //fragmentTransaction.replace(R.id.frame_layout_code_queue, fragment)
        activity?.supportFragmentManager?.popBackStack()
        fragmentTransaction.commit()
    }
}