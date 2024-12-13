package com.shrutipandit.cardprintsmart.uiFragment

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
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
import com.shrutipandit.cardprintsmart.card.ApplicationPamplet
import com.shrutipandit.cardprintsmart.databinding.FragmentDemoApplicationMakerBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DemoApplicationMakerFragment : Fragment(R.layout.fragment_demo_application_maker) {

    private lateinit var binding: FragmentDemoApplicationMakerBinding
    private val applicationPamplet = ApplicationPamplet()
    private var pdfBytes1: ByteArray? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDemoApplicationMakerBinding.bind(view)

        // Check and request permissions
        checkAndRequestPermissions()

        // Extract arguments passed from previous fragment
        val args = arguments?.let { DemoApplicationMakerFragmentArgs.fromBundle(it) }

        // Extract school data
        val schoolData = listOf(
            args?.to ?: "To Missing",
            args?.officeAddress ?: "School Address Missing",
            args?.subject ?: "Subject Missing",
            args?.sirorMam ?: "Sir/Mam Missing",
            args?.body ?: "Sir/Mam Missing",
            args?.applicantName ?: "Student Name Missing",
            args?.date ?: "Date Missing"
        )

        // Generate the PDF for school application
        generateSchoolApplicationPdf(schoolData)

        // Set the save button functionality
        binding.pdfBtn1.setOnClickListener {
            val fileName = "School_Application.pdf"
            pdfBytes1?.let { bytes ->
                if (savePdfToDownloads(requireContext(), bytes, fileName)) {
                    showToast("PDF saved as $fileName")
                } else {
                    showToast("Failed to save PDF")
                }
            }
        }
    }

    private fun generateSchoolApplicationPdf(schoolData: List<String>) {
        pdfBytes1 = applicationPamplet.generateApplicationPdf(requireContext(), schoolData)
        pdfBytes1?.let { bytes ->
            binding.pdfView1.fromBytes(bytes)
                .swipeHorizontal(false)
                .load()
        } ?: showToast("Failed to generate School Application PDF")
    }

    private fun savePdfToDownloads(context: Context, pdfBytes: ByteArray, fileName: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            savePdfToDownloadsScoped(context, pdfBytes, fileName)
        } else {
            savePdfToDownloadsLegacy(context, pdfBytes, fileName)
        }
    }

    private fun savePdfToDownloadsLegacy(context: Context, pdfBytes: ByteArray, fileName: String): Boolean {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDir.exists() && !downloadsDir.mkdirs()) {
            return false
        }

        val file = File(downloadsDir, fileName)
        return try {
            FileOutputStream(file).use { fos -> fos.write(pdfBytes) }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun savePdfToDownloadsScoped(context: Context, pdfBytes: ByteArray, fileName: String): Boolean {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            return try {
                resolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(pdfBytes)
                } != null
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }
        return false
    }

    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
