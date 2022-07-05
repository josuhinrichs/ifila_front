package com.example.ifila_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ifila_app.databinding.FragmentQueuePositionBinding
import com.example.ifila_app.databinding.FragmentUserQueueBinding

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
    private var BUSINESS_NAME: String? = null
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
        BUSINESS_NAME = arguments?.getString("nome_estabelecimento")
        CODE = arguments?.getString("codigo")

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
}