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

class JatiPramanPatraFragment : Fragment(R.layout.fragment_aay_praman_patra) {
    private lateinit var binding: FragmentAayPramanPatraBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAayPramanPatraBinding.bind(view)

        val linearLayout = binding.linearLayout

        // Fields to add dynamically
        val jatipramnPatraList = arrayListOf(
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
            "Post Office    :",
            "Police Station    :",
            "Prakhanad    :",
            "Anumnadal    :",
            "Caste    :",
            "Anusuchi    :",
            "Anukramank    :",
            "Digitally Signed By    :",
            "Date and Time   :"
        )

        val editTexts = mutableListOf<TextInputEditText>()

        // Add TextInputEditTexts dynamically
        for (label in jatipramnPatraList) {
            if (label == "Name    :" || label == "Father Name    :" || label == "Mother Name    :") {
                // Add English input field
                val englishEditText = TextInputEditText(requireContext()).apply {
                    hint = "$label (English)"
                }
                linearLayout.addView(
                    englishEditText,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                editTexts.add(englishEditText)

                // Add Hindi input field
                val hindiEditText = TextInputEditText(requireContext()).apply {
                    hint = "$label (Hindi)"
                }
                linearLayout.addView(
                    hindiEditText,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                editTexts.add(hindiEditText)
            } else {
                // Add other fields as usual
                val textInputEditText = TextInputEditText(requireContext()).apply {
                    hint = label
                }
                linearLayout.addView(
                    textInputEditText,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                editTexts.add(textInputEditText)
            }
        }

        // Add the submit button
        val submitButton = Button(requireContext()).apply {
            text = "Submit"
            setOnClickListener {
                val formNumber = editTexts[0].text.toString()
                val district = editTexts[1].text.toString()
                val anumandal = editTexts[2].text.toString()
                val circle = editTexts[3].text.toString()
                val pramanPatranumber = editTexts[4].text.toString()
                val date = editTexts[5].text.toString()
                val nameEnglish = editTexts[6].text.toString() // Name (English)
                val nameHindi = editTexts[7].text.toString()   // Name (Hindi)
                val fatherNameEnglish = editTexts[8].text.toString() // Father Name (English)
                val fatherNameHindi = editTexts[9].text.toString()   // Father Name (Hindi)
                val motherNameEnglish = editTexts[10].text.toString() // Mother Name (English)
                val motherNameHindi = editTexts[11].text.toString()   // Mother Name (Hindi)
                val village = editTexts[12].text.toString()
                val postOffice = editTexts[13].text.toString()
                val policeStation = editTexts[14].text.toString()
                val prakhanad = editTexts[15].text.toString()
                val anumnadal = editTexts[16].text.toString()
                val caste = editTexts[17].text.toString()
                val anusuchi = editTexts[18].text.toString()
                val anukramank = editTexts[19].text.toString()
                val digitallySignedBy = editTexts[20].text.toString()
                val dateAndTime = editTexts[21].text.toString()

                // Navigate and pass data
                val action = JatiPramanPatraFragmentDirections.actionAayPramanPatraFragmentToDemoJatiPramanPatraFragment(
                    formNumber, district, anumandal, circle, pramanPatranumber, date,
                    "$nameEnglish / $nameHindi", // Combined Name
                    "$fatherNameEnglish / $fatherNameHindi", // Combined Father Name
                    "$motherNameEnglish / $motherNameHindi", // Combined Mother Name
                    village, postOffice, policeStation, prakhanad, anumnadal, caste,
                    anusuchi, anukramank, digitallySignedBy, dateAndTime
                )
                findNavController().navigate(action)
            }
        }

        linearLayout.addView(
            submitButton,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
