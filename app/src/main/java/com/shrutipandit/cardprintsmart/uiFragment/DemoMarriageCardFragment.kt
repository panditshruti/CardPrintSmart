package com.shrutipandit.cardprintsmart.uiFragment

import android.content.Context
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.card.MarriageBioData
import com.shrutipandit.cardprintsmart.card.PdfandPrint
import com.shrutipandit.cardprintsmart.databinding.FragmentDemoMarriageCardBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DemoMarriageCardFragment : Fragment(R.layout.fragment_demo_marriage_card) {
    private lateinit var binding: FragmentDemoMarriageCardBinding
    private val marriageBioData = MarriageBioData()
    private val pdfAndPrint = PdfandPrint()
    private var pdfBytes: ByteArray? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDemoMarriageCardBinding.bind(view)

        // Get the data passed from MarriageBioDataFragment
        val args = arguments?.let { DemoMarriageCardFragmentArgs.fromBundle(it) }
        val editTextData = args?.editTextData?.split(",")?.toMutableList()

        if (editTextData != null) {
            // Set the data and generate the PDF
            marriageBioData.setData(editTextData)
            pdfBytes = marriageBioData.generatePdf(requireContext())

            // Load the PDF into the PDFView
            binding.pdfView.fromBytes(pdfBytes).load()
        }

        binding.pdfBtn.setOnClickListener {
            pdfBytes?.let { bytes ->
                if (savePdf(requireContext(), bytes)) {
                    showToast("PDF saved successfully")
                } else {
                    showToast("Failed to save PDF")
                }
            } ?: showToast("No PDF to save")
        }
    }

    private fun savePdf(context: Context, pdfBytes: ByteArray): Boolean {
        // Create a directory for the PDF file
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "PDFs")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        // Create the PDF file
        val file = File(directory, "e_marriage.pdf")

        return try {
            FileOutputStream(file).use { fos ->
                fos.write(pdfBytes)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}