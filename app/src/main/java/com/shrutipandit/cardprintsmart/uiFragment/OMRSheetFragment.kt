package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentOMRSheetBinding

class OMRSheetFragment : Fragment(R.layout.fragment_o_m_r_sheet) {

    private lateinit var binding: FragmentOMRSheetBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOMRSheetBinding.bind(view)

        binding.btnGenerateOMRSheet.setOnClickListener {
            val numberOfQuestions = binding.etNumberOfQuestions.text.toString().toIntOrNull()

            if (numberOfQuestions != null && numberOfQuestions > 0) {
                val action = OMRSheetFragmentDirections.actionOMRSheetFragmentToDemoOMRSheetFragment(
                    numberOfQuestions
                )
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Please enter a valid number of questions", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
