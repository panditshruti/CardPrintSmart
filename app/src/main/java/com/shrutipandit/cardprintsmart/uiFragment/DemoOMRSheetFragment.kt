package com.shrutipandit.cardprintsmart.uiFragment

import OMRSheet
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentDemoOMRSheetBinding
import java.io.IOException

class DemoOMRSheetFragment : Fragment(R.layout.fragment_demo_o_m_r_sheet) {

    private lateinit var binding: FragmentDemoOMRSheetBinding
    private val args: DemoOMRSheetFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDemoOMRSheetBinding.bind(view)

        // Retrieve numberOfQuestions and paperSize from arguments
        val numberOfQuestions = args.numberOfQuestions
        val paperSize = args.paperSize

        // Generate the OMR sheet PDF
        val pdfBytes = generatePdf(requireContext(), numberOfQuestions, paperSize)

        // Display the generated PDF in the PDFView
        binding.pdfView.fromBytes(pdfBytes).load()

        // Set up the Save button click listener
        binding.pdfBtn.setOnClickListener {
            savePdf(pdfBytes)
        }
    }

    /**
     * Generates the OMR sheet PDF as a ByteArray.
     * @param context The context for PDF generation.
     * @param numberOfQuestions The total number of questions to include.
     * @param paperSize The desired paper size for the PDF.
     * @return A ByteArray containing the generated PDF data.
     */
    private fun generatePdf(context: Context, numberOfQuestions: Int, paperSize: String): ByteArray {
        val omrSheet = OMRSheet()
        return omrSheet.generatePdf(context, numberOfQuestions, paperSize)
    }

    /**
     * Saves the generated PDF to the device's external storage.
     * @param pdfBytes The PDF data as a ByteArray.
     */
    private fun savePdf(pdfBytes: ByteArray) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "e_omr_sheet.pdf") // Set the file name
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf") // Set the MIME type
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/PDFs") // Save location
        }

        val resolver = requireContext().contentResolver
        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

        if (uri != null) {
            try {
                resolver.openOutputStream(uri).use { fos ->
                    fos?.write(pdfBytes)
                    Toast.makeText(requireContext(), "PDF saved successfully at ${uri.path}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to save PDF: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Failed to create file URI", Toast.LENGTH_SHORT).show()
        }
    }
}
