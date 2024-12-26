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
        for (label in jatipramnPatraList) {
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
            val formNumber = editTexts[0].text.toString()
            val district = editTexts[1].text.toString()
            val anumandal = editTexts[2].text.toString()
            val circle = editTexts[3].text.toString()
            val pramanPatranumber = editTexts[4].text.toString()
            val date = editTexts[5].text.toString()
            val name = editTexts[6].text.toString()
            val fatherName = editTexts[7].text.toString()
            val motherName = editTexts[8].text.toString()
            val village = editTexts[9].text.toString()
            val postoffice = editTexts[11].text.toString()
            val policeStation = editTexts[11].text.toString()
            val prakhanad = editTexts[12].text.toString()
            val anumnadal = editTexts[13].text.toString()
            val caste = editTexts[14].text.toString()
            val anusuchi = editTexts[15].text.toString()
            val anukramank = editTexts[16].text.toString()
            val digitallySignedBy = editTexts[17].text.toString()
            val dateandtime = editTexts[18].text.toString()

            // Navigate and pass data
            val action = JatiPramanPatraFragmentDirections.actionAayPramanPatraFragmentToDemoJatiPramanPatraFragment(
                formNumber,district,anumandal,circle,pramanPatranumber,date,name, fatherName, motherName,village,postoffice,policeStation,prakhanad,anumnadal,caste,
                anusuchi,anukramank,digitallySignedBy,dateandtime)
            findNavController().navigate(action)
        }

        linearLayout.addView(
            submitButton,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}