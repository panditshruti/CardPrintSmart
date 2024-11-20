package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
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

        // Setting up spinner for paper sizes
        val paperSizes = arrayOf("A4", "A3", "A5")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paperSizes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.paperSizeSpinner.adapter = adapter

        binding.generateButton.setOnClickListener {
            val numberOfQuestions = binding.numberOfQuestionsInput.text.toString().toIntOrNull()
            val paperSize = binding.paperSizeSpinner.selectedItem.toString()

            if (numberOfQuestions != null && numberOfQuestions > 0) {
                val action = OMRSheetFragmentDirections.actionOMRSheetFragmentToDemoOMRSheetFragment(
                    numberOfQuestions,
                    paperSize
                )
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Please enter a valid number of questions", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
