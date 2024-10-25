package com.shrutipandit.cardprintsmart.uiFragment

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentQuestionMakerDetailsBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class QuestionMakerDetailsFragment : Fragment(R.layout.fragment_question_maker_details) {

    private lateinit var binding: FragmentQuestionMakerDetailsBinding
    private var pdfBytes: ByteArray? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuestionMakerDetailsBinding.bind(view)

        // Check and request necessary permissions
        checkAndRequestPermissions()

        // Generate the PDF
        pdfBytes = generatePdf() // Actual PDF generation

        // Display the PDF in the PDFView
        pdfBytes?.let { bytes ->
            binding.pdfView.fromBytes(bytes).load()
        } ?: showToast("Failed to generate PDF")

        // Save PDF button click handler
        binding.pdfBtn.setOnClickListener {
            pdfBytes?.let { bytes ->
                if (savePdfToDownloads(requireContext(), bytes)) {
                    showToast("PDF saved successfully in Downloads")
                } else {
                    showToast("Failed to save PDF")
                }
            } ?: showToast("No PDF to save")
        }
    }

    private fun generatePdf(): ByteArray {
        // Create a new PDF document
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        // Draw text on the page
        val canvas = page.canvas


        // Finish the page
        pdfDocument.finishPage(page)

        // Write the document to a byte array
        val stream = ByteArrayOutputStream()
        pdfDocument.writeTo(stream)
        pdfDocument.close()

        return stream.toByteArray()
    }

    private fun savePdfToDownloads(context: Context, pdfBytes: ByteArray): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            savePdfToDownloadsScoped(context, pdfBytes)
        } else {
            savePdfToDownloadsLegacy(context, pdfBytes)
        }
    }

    private fun savePdfToDownloadsLegacy(context: Context, pdfBytes: ByteArray): Boolean {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDir.exists() && !downloadsDir.mkdirs()) {
            return false
        }

        val file = File(downloadsDir, "e_question_card.pdf")
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

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun savePdfToDownloadsScoped(context: Context, pdfBytes: ByteArray): Boolean {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "e_question_card.pdf")
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            return try {
                resolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(pdfBytes)
                    outputStream.close()
                    true
                } ?: false
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }
        return false
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // Add WRITE_EXTERNAL_STORAGE permission for versions below Android Q
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), permissionsToRequest.toTypedArray(), REQUEST_CODE_PERMISSIONS)
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 1
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                showToast("Permissions granted")
            } else {
                showToast("Permissions denied")
            }
        }
    }
}
