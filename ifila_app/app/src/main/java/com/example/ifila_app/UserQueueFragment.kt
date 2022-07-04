package com.example.ifila_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.ifila_app.databinding.FragmentUserCodeQueueBinding
import com.example.ifila_app.databinding.FragmentUserQueueBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "token"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserQueueFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserQueueFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var TOKEN: String? = null
    private var BUSINESS_NAME: String? = null
    private var QUEUE_STATUS: String? = null
    private lateinit var binding: FragmentUserQueueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserQueueBinding.inflate(layoutInflater)
        //binding.buttonBuscarEst.setOnClickListener { startCodRequest()}

        val view_view = inflater.inflate(R.layout.fragment_user_queue, container, false)
        //val botao = view_view.findViewById<Button>(R.id.button_buscarEst)
        //botao.setOnClickListener { startCodRequest()}
        TOKEN = arguments?.getString("token")
        BUSINESS_NAME = arguments?.getString("nome_estabelecimento")
        QUEUE_STATUS = arguments?.getString("fila_status")

        val business_name = view_view.findViewById<TextView>(R.id.text_business_name)
        val business_status = view_view.findViewById<TextView>(R.id.text_business_status)
        business_name.text = BUSINESS_NAME
        if(QUEUE_STATUS.toBoolean()){
            business_status.text = "Aberto"
        }else {
            business_status.text = "Fechado"
            business_status.setTextColor(resources.getColor(R.color.red_main))
        }


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
}