package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentMarriagePampletBinding

class MarriagePampletFragment : Fragment(R.layout.fragment_marriage_pamplet) {
    private lateinit var binding: FragmentMarriagePampletBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMarriagePampletBinding.bind(view)

        val linearLayout = binding.linearLayout

        // Fields to add dynamically
        val marriageArrayList = arrayListOf(
            "Dulha Name    :",
            "Dulhan Name    :",
            "Date    :",
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
