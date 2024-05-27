package com.shrutipandit.cardprintsmart.uiFragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.card.OMRSheet
import com.shrutipandit.cardprintsmart.databinding.FragmentDemoOMRSheetBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DemoOMRSheetFragment : Fragment(R.layout.fragment_demo_o_m_r_sheet) {

    private lateinit var binding: FragmentDemoOMRSheetBinding
    private val args: DemoOMRSheetFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDemoOMRSheetBinding.bind(view)

        val numberOfQuestions = args.numberOfQuestions
        val paperSize = args.paperSize

        val pdfBytes = generatePdf(requireContext(), numberOfQuestions, paperSize)
        binding.pdfView.fromBytes(pdfBytes).load()

        binding.savePdfButton.setOnClickListener {
            savePdf(pdfBytes)
        }
    }

    private fun generatePdf(context: Context, numberOfQuestions: Int, paperSize: String): ByteArray {
        val omrSheet = OMRSheet()
        return omrSheet.generatePdf(context, numberOfQuestions, paperSize)
    }

    private fun savePdf(pdfBytes: ByteArray) {
        val directory = File(requireContext().getExternalFilesDir(null), "PDFs")
        if (!directory.exists() && !directory.mkdirs()) {
            Toast.makeText(requireContext(), "Failed to create directory", Toast.LENGTH_SHORT).show()
            return
        }

        val file = File(directory, "omr_sheet.pdf")
        try {
            FileOutputStream(file).use { fos ->
                fos.write(pdfBytes)
                Toast.makeText(requireContext(), "PDF saved successfully", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "PDF failed to save", Toast.LENGTH_SHORT).show()
        }
    }
}
