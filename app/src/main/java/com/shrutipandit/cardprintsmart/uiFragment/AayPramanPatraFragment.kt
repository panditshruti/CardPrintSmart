package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentAayPramanPatraBinding
import com.shrutipandit.cardprintsmart.databinding.FragmentMarriagePampletBinding

class AayPramanPatraFragment : Fragment(R.layout.fragment_aay_praman_patra) {
    private lateinit var binding: FragmentAayPramanPatraBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAayPramanPatraBinding.bind(view)

        val linearLayout = binding.linearLayout

        // Fields to add dynamically
        val marriageArrayList = arrayListOf(
            "Form Number   :",
            "District    :",
            "Anumandal    :",
            "Circle    :",
            "PramanPatra number    :",
            "Date    :",
            "Name    :",
            "Father Name    :",
            "Mother Name    :",
            "Village    :",
            "postOffice    :",
            "PoliceStation    :",
            "Prakhanad    :",
            "Anumnadal    :",
            "caste    :",
            "anusuchi    :",
            "anukramank    :",
            "Digitally Signed By    :",
            "Date and time   :",

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
            val action = MarriagePampletFragmentDirections.actionMarriagePampletFragmentToDemoMarraigePampletFragment(
                dulhaName, dulhanName,date)
            findNavController().navigate(action)
        }

        linearLayout.addView(
            submitButton,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
