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

        // Fields to add dynamically
        val marriageArrayList = arrayListOf(
            "Enter To (Principal,ClassTeacher)   :",
            "Enter Shool Name    :",
            "Enter School Address    :",
            "Enter Subject full details   :",
            "Enter Sir ya mam",
            "Enter School/collage/Institute",
            "Enter absent fist to last date",
            "Enter Student Name",
            "Enter Class",
            "Enter rollno",
            "Enter date",
        )

        val editTexts = mutableListOf<TextInputEditText>()

        // Add TextInputEditTexts for each item in the marriageArrayList
        for (label in marriageArrayList) {
            val textInputEditText = TextInputEditText(requireContext())
            textInputEditText.hint = label
            linearLayout.addView(
                textInputEditText,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            editTexts.add(textInputEditText)
        }

        // Add the submit button
        val submitButton = Button(requireContext())
        submitButton.text = "Submit"
        submitButton.setOnClickListener {
            val dulhaName = editTexts[0].text.toString()
            val dulhanName = editTexts[1].text.toString()
            val date = editTexts[2].text.toString()

            // Navigate and pass data
            val action = ApplicationMakerFragmentDirections.actionApplicationMakerFragmentToDemoApplicationMakerFragment(dulhaName, dulhanName,date)

            findNavController().navigate(action)
        }

        linearLayout.addView(
            submitButton,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
