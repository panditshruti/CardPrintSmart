package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

        // School fields
        val schoolFields = arrayListOf(
            "Enter To (Officer's Name):",
            "Enter Office Address:",
            "Enter Subject full details:",
            "Enter Sir/Mam:",
            "Enter Body:",
            "Enter Applicant Name:",
            "Enter Date:"
        )

        val schoolEditTexts = mutableListOf<TextInputEditText>()

        // Function to create EditTexts dynamically
        fun createEditTexts(fields: List<String>, targetList: MutableList<TextInputEditText>) {
            for (label in fields) {
                val editText = TextInputEditText(requireContext())
                editText.hint = label
                editText.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                targetList.add(editText)
                linearLayout.addView(editText) // Add EditText to the layout
            }
        }

        // Create School EditTexts
        createEditTexts(schoolFields, schoolEditTexts)

        // Add Submit button
        val submitButton = Button(requireContext())
        submitButton.text = "Submit"
        submitButton.setOnClickListener{
            val schoolData = schoolEditTexts.map { it.text.toString() }
            // Navigate to DemoApplicationMakerFragment with the entered data
            val action = ApplicationMakerFragmentDirections.actionApplicationMakerFragmentToDemoApplicationMakerFragment(
                schoolData[0], schoolData[1], schoolData[2], schoolData[3],
                schoolData[4],schoolData[5], schoolData[6]
            )
            findNavController().navigate(action)
        }

        linearLayout.addView(submitButton) // Add Submit button to the layout
    }
}
