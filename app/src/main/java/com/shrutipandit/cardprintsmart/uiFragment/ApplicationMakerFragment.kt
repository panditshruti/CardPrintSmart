package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentApplicationMakerBinding

class ApplicationMakerFragment : Fragment(R.layout.fragment_application_maker) {
    private lateinit var binding: FragmentApplicationMakerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentApplicationMakerBinding.bind(view)

        val linearLayout = binding.linearLayout

        // Fields for application input
        val inputFields = listOf(
            "Enter To (Officer's Name):",
            "Enter Office Address:",
            "Enter Subject full details:",
            "Enter Sir/Mam:",
            "Enter Body:",
            "Enter Applicant Name:",
            "Enter Date:"
        )

        val inputEditTexts = mutableListOf<TextInputEditText>()

        // Dynamically create EditTexts
        inputFields.forEach { label ->
            val editText = TextInputEditText(requireContext()).apply {
                hint = label
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            inputEditTexts.add(editText)
            linearLayout.addView(editText)
        }

        // Add Submit button
        val submitButton = Button(requireContext()).apply {
            text = "Submit"
            setOnClickListener {
                val inputData = inputEditTexts.map { it.text.toString() }
                val action = ApplicationMakerFragmentDirections.actionApplicationMakerFragmentToDemoApplicationMakerFragment(
                    inputData[0], inputData[1], inputData[2], inputData[3],
                    inputData[4], inputData[5], inputData[6]
                )
                findNavController().navigate(action)
            }
        }

        linearLayout.addView(submitButton)
    }
}
