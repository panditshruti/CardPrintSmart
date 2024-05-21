package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentMarriageBioDataBinding

class MarriageBioDataFragment : Fragment(R.layout.fragment_marriage_bio_data) {
    private lateinit var binding: FragmentMarriageBioDataBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMarriageBioDataBinding.bind(view)

        // Get the linear layout from the binding
        val linearLayout = binding.linearLayout

        val marriageArrayList = arrayListOf(
            "Name             :",
            "Date Of Birth    :",
            "Birth Place      :",
            "Religion         :",
            "Caste            :",
            "Height           :",
            "Blood Group      :",
            "Complexion       :",
            "Education        :",
            "10 Marks         :",
            "12 Marks         :",
            "Post             :",
            "District         :",
            "State            :",
            "Family Information",
            "Father Name      :",
            "Occupation       :",
            "Mother Name      :",
            "Occupation       :",
            "Sister           :",
            "Brother          :",
            "Contact Information",
            "Address          :",
            "Contact No-      :"
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
        submitButton.setOnClickListener {
            val data = editTexts.joinToString(",") { it.text.toString() }
            val action = MarriageBioDataFragmentDirections.actionMarriageBioDataFragmentToDemoMarriageCardFragment(data)
            findNavController().navigate(action)
        }

        linearLayout.addView(submitButton, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
