package com.shrutipandit.cardprintsmart.uiFragment

import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.card.BioData
import com.shrutipandit.cardprintsmart.databinding.FragmentDemoBioDataBinding
import java.io.IOException

class DemoBioDataFragment : Fragment(R.layout.fragment_demo_bio_data) {

    private lateinit var binding: FragmentDemoBioDataBinding
    private val args: DemoBioDataFragmentArgs by navArgs()

    private var pdfBytes: ByteArray? = null
    private lateinit var bioData: BioData

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDemoBioDataBinding.bind(view)

        bioData = BioData()

        val editTextData = args.editTextData?.split(",")?.toMutableList()
        val imageUriString = args.image
        val selectedImageUri = if (imageUriString.isNullOrEmpty()) null else Uri.parse(imageUriString)

        if (editTextData != null) {
            bioData.setData(editTextData)
        }

        selectedImageUri?.let { uri ->
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            pdfBytes = bioData.generatePdf(requireContext(), bitmap)
            binding.pdfView.fromBytes(pdfBytes).load()
        }

        binding.pdfBtn.setOnClickListener {
            pdfBytes?.let { bytes ->
                savePdf(bytes)
            } ?: run {
                Toast.makeText(requireContext(), "PDF is not generated yet!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun savePdf(pdfBytes: ByteArray) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "e_bioData_card.pdf")
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/PDFs")
        }

        val resolver = requireContext().contentResolver
        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

        if (uri != null) {
            try {
                resolver.openOutputStream(uri).use { outputStream ->
                    outputStream?.write(pdfBytes)
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
