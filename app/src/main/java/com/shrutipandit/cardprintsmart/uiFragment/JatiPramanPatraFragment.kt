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
            "फौर्म नम्बर   :",
            "जिला  :",
            "अनुमंदल    :",
            "अंचल    :",
            "प्रमाण-पत्र संखया    :",
            "दिनांक   :",
            "नाम    :",
            "Name    :",
            "पिता नाम     :",
            "Father Name    :",
            "माता नाम    :",
            "Mother Name    :",
            "ग्राम    :",
            "डाकघर    :",
            "थाना    :",
            "प्रखंड    :",
            "जाति    :",
            "अनुसूची    :",
            "अनुक्रमांक    :",
            "Digitally Signed By    :",
            "Date and Time   :"
        )

        val editTexts = mutableListOf<TextInputEditText>()

        // Add TextInputEditTexts dynamically
        for (label in jatipramnPatraList) {
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
                val name = editTexts[6].text.toString()
                val hindiName = editTexts[7].text.toString()
                val fatherName = editTexts[8].text.toString()
                val hindiFatherName = editTexts[9].text.toString()
                val motherName = editTexts[10].text.toString()
                val hindiMotherName = editTexts[11].text.toString()
                val village = editTexts[12].text.toString()
                val postOffice = editTexts[13].text.toString()
                val policeStation = editTexts[14].text.toString()
                val prakhanad = editTexts[15].text.toString()
                val caste = editTexts[16].text.toString()
                val anusuchi = editTexts[17].text.toString()
                val anukramank = editTexts[18].text.toString()
                val digitallySignedBy = editTexts[19].text.toString()
                val dateAndTime = editTexts[20].text.toString()

                // Navigate and pass data
                val action = JatiPramanPatraFragmentDirections.actionAayPramanPatraFragmentToDemoJatiPramanPatraFragment(
                    formNumber, district, anumandal, circle, pramanPatranumber, date, name, hindiName,
                    fatherName, hindiFatherName, motherName, hindiMotherName, village, postOffice,
                    policeStation, prakhanad, caste, anusuchi, anukramank, digitallySignedBy, dateAndTime
                )
                findNavController().navigate(action)

                // Clear all EditText fields
                for (editText in editTexts) {
                    editText.text?.clear()
                }
            }
        }

        linearLayout.addView(
            submitButton,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
