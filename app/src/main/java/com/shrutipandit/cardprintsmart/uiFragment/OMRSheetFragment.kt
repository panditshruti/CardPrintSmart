package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        // Show character count for school name input
        binding.schoolNameInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val charCount = s?.length ?: 0
                val maxLength = 50
                // Display character count below the EditText
                binding.schoolNameCharCount.text = "$charCount/$maxLength"
            }

            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Handling generate button click
        binding.generateButton.setOnClickListener {
            val numberOfQuestions = binding.numberOfQuestionsInput.text.toString().toIntOrNull()
            val paperSize = binding.paperSizeSpinner.selectedItem.toString()
            val schoolName = binding.schoolNameInput.text.toString().trim()

            // Validate inputs
            if (numberOfQuestions != null && numberOfQuestions > 0 && schoolName.isNotEmpty()) {
                val action = OMRSheetFragmentDirections.actionOMRSheetFragmentToDemoOMRSheetFragment(
                    numberOfQuestions,
                    paperSize,
                    schoolName // Pass the school name
                )
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
