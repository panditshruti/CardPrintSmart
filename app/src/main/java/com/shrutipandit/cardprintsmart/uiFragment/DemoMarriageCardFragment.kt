package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.card.MarriageBioData
import com.shrutipandit.cardprintsmart.databinding.FragmentDemoMarriageCardBinding

class DemoMarriageCardFragment : Fragment(R.layout.fragment_demo_marriage_card) {
    private lateinit var binding: FragmentDemoMarriageCardBinding
    private val marriageBioData = MarriageBioData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDemoMarriageCardBinding.bind(view)

        // Set your data here
        val data = mutableListOf(
            "John Doe", "01/01/1990", "New York", "Christian", "Caste", "6 feet",
            "O+", "Fair", "MSc", "85%", "90%", "Engineer", "NYC", "NY", "Mr. Doe",
            "Engineer", "Mrs. Doe", "Doctor", "2", "1", "123 Street, NY", "1234567890"
        )
        marriageBioData.setData(data)

        // Generate the PDF byte array
        val pdfBytes = marriageBioData.generatePdf(requireContext())

        // Load PDF content into PDFView
        binding.pdfView.fromBytes(pdfBytes).load()
    }
}
