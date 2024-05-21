package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.github.barteksc.pdfviewer.PDFView
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.card.MarriageBioData
import com.shrutipandit.cardprintsmart.databinding.FragmentDemoMarriageCardBinding

class DemoMarriageCardFragment : Fragment(R.layout.fragment_demo_marriage_card) {
    private lateinit var binding: FragmentDemoMarriageCardBinding
    private val marriageBioData = MarriageBioData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDemoMarriageCardBinding.bind(view)

        // Get the data passed from MarriageBioDataFragment
        val args = arguments?.let { DemoMarriageCardFragmentArgs.fromBundle(it) }
        val editTextData = args?.editTextData?.split(",")?.toMutableList()

        if (editTextData != null) {
            // Set the data and generate the PDF
            marriageBioData.setData(editTextData)
            val pdfBytes = marriageBioData.generatePdf(requireContext())

            // Load the PDF into the PDFView
            binding.pdfView.fromBytes(pdfBytes).load()
        }
    }
}
