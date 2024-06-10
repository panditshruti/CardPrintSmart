package com.shrutipandit.cardprintsmart.uiFragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentBioDetailsBinding

class BioDetailsFragment : Fragment(R.layout.fragment_bio_details) {
    private lateinit var binding: FragmentBioDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBioDetailsBinding.bind(view)

        val linearLayout = binding.linearLayout

        val marriageArrayList = arrayListOf(
            "Name                 :",
            "Father's Name        :",
            "Gender               :",
            "Caste                :",
            "Height               :",
            "Blood Group          :",
            "Date Of Birth        :",
            "Mobile No-           :",
            "Marital Status       :",
            "Email Id             :",
            "Religion             :",
            "Nationality          :",
            "Address              :",
            "Languages Knows      :",
            "Education            :",
            "Computer Knowledge   :",
            "Work                 :",
            "Work Experience      :",
            "Declaration          :",
            "Place                :",
            "Date                 :"
        )

        val editTexts = mutableListOf<TextInputEditText>()

        // Add TextInputEditTexts to the layout for each item in the marriageArrayList
        for (label in marriageArrayList) {
            val textInputEditText = TextInputEditText(requireContext())
            textInputEditText.hint = label
            linearLayout.addView(textInputEditText, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            editTexts.add(textInputEditText)
        }

        // Add the submit button at the end of the linear layout
        val submitButton = Button(requireContext())
        submitButton.text = "Submit"
        submitButton.setBackgroundColor(Color.RED)
        submitButton.setOnClickListener {
            val data = editTexts.joinToString(",") { it.text.toString() }
            val action = BioDetailsFragmentDirections.actionBioDetailsFragmentToDemoBioDataFragment(data)
            findNavController().navigate(action)
        }

        linearLayout.addView(submitButton, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
