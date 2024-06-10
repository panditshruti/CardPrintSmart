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

        binding.pdfBtn.setOnClickListener {
            savePdf(pdfBytes)
        }
    }

    private fun generatePdf(context: Context, numberOfQuestions: Int, paperSize: String): ByteArray {
        val omrSheet = OMRSheet()
        return omrSheet.generatePdf(context, numberOfQuestions, paperSize)
    }

    private fun savePdf(pdfBytes: ByteArray) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "e_omr_sheet.pdf")
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/PDFs")
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
                Toast.makeText(requireContext(), "PDF failed to save", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Failed to create file", Toast.LENGTH_SHORT).show()
        }
    }
}
