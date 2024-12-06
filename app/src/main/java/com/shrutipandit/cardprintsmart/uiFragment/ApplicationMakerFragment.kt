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
        val spinner = binding.spinner // Add a Spinner in your XML layout and bind it here

        // Add spinner options
        val spinnerOptions = arrayOf("Select Application Type", "School Application", "Officer Application")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        // School fields
        val schoolFields = arrayListOf(
            "Enter To (Principal, Class Teacher):",
            "Enter School Name:",
            "Enter School Address:",
            "Enter Subject full details:",
            "Enter Sir or Mam:",
            "Enter absent date range:",
            "Enter Student Name:",
            "Enter Class:",
            "Enter Roll No:",
            "Enter Date:"
        )

        // Officer fields
        val officerFields = arrayListOf(
            "Enter To (Officer's Name):",
            "Enter Department Name:",
            "Enter Office Address:",
            "Enter Subject full details:",
            "Enter Body:",
            "Enter Applicant Name:",
            "Enter Date:"
        )

        val schoolEditTexts = mutableListOf<TextInputEditText>()
        val officerEditTexts = mutableListOf<TextInputEditText>()

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
            }
        }

        // Create School and Officer EditTexts
        createEditTexts(schoolFields, schoolEditTexts)
        createEditTexts(officerFields, officerEditTexts)

        // Function to update visible fields
        fun updateFields(selectedOption: String) {
            linearLayout.removeAllViews()

            when (selectedOption) {
                "School Application" -> {
                    schoolEditTexts.forEach { linearLayout.addView(it) }
                }
                "Officer Application" -> {
                    officerEditTexts.forEach { linearLayout.addView(it) }
                }
            }

            // Add the Submit button
            val submitButton = Button(requireContext())
            submitButton.text = "Submit"
            submitButton.setOnClickListener {
                when (selectedOption) {
                    "School Application" -> {
                        val schoolData = schoolEditTexts.map { it.text.toString() }
                        val action = ApplicationMakerFragmentDirections.actionApplicationMakerFragmentToDemoApplicationMakerFragment(
                            schoolData[0], schoolData[1], schoolData[2], schoolData[3],
                            schoolData[4], "", schoolData[5], schoolData[6], schoolData[7],
                            schoolData[8], schoolData[9]
                        )
                        findNavController().navigate(action)
                    }
                    "Officer Application" -> {
                        val officerData = officerEditTexts.map { it.text.toString() }
                        val action = ApplicationMakerFragmentDirections.actionApplicationMakerFragmentToDemoApplicationMakerFragment(
                            officerData[0], officerData[1], officerData[2], officerData[3],
                            "", officerData[4], officerData[5], "", "", "", officerData[6]
                        )
                        findNavController().navigate(action)
                    }
                }
            }

            linearLayout.addView(submitButton)
        }

        // Spinner selection listener
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOption = spinnerOptions[position]
                updateFields(selectedOption)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}
