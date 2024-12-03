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
            "Enter School Name    :",
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
            val to = editTexts[0].text.toString()
            val shoolName = editTexts[1].text.toString()
            val schoolAddress = editTexts[2].text.toString()
            val subject = editTexts[3].text.toString()
            val sirMam = editTexts[4].text.toString()
            val sci = editTexts[5].text.toString()
            val absentDate = editTexts[6].text.toString()
            val studentName = editTexts[7].text.toString()
            val sclass = editTexts[8].text.toString()
            val rollno = editTexts[9].text.toString()
            val date = editTexts[10].text.toString()

            val action = ApplicationMakerFragmentDirections.actionApplicationMakerFragmentToDemoApplicationMakerFragment2(to,shoolName,schoolAddress,
                subject,sirMam,sci,absentDate,studentName,sclass,rollno,date,)

            findNavController().navigate(action)
        }

        linearLayout.addView(
            submitButton,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
