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
import com.shrutipandit.cardprintsmart.card.MarriagePamplet
import com.shrutipandit.cardprintsmart.databinding.FragmentDemoApplicationMakerBinding
import com.shrutipandit.cardprintsmart.databinding.FragmentDemoMarraigePampletBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DemoApplicationMakerFragment : Fragment(R.layout.fragment_demo_application_maker) {

    private lateinit var binding: FragmentDemoApplicationMakerBinding
    private val applicationPamplet = ApplicationPamplet()
    private var pdfBytes1: ByteArray? = null
    private var pdfBytes2: ByteArray? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDemoApplicationMakerBinding.bind(view)

        // Check and request permissions
        checkAndRequestPermissions()

        // Extract arguments passed from MarriagePampletFragment
        val args = arguments?.let { DemoApplicationMakerFragmentArgs.fromBundle(it) }
        val to = args?.to ?: "To Missing"
        val schoolName = args?.schoolName ?: "School Name Missing"
        val schoolAddress = args?.schoolAddress ?: "School Address Missing"
        val subject = args?.subject ?: "Subject Missing"
        val sirMam = args?.sirorMam ?: "Sir/Mam Missing"
        val sci = args?.schoolCI ?: "School/Institute Missing"
        val absentDate = args?.absentdate ?: "Absent Date Missing"
        val studentName = args?.studentName ?: "Student Name Missing"
        val clask = args?.clask ?: "Class Missing"
        val rollno = args?.rollno ?: "Roll Number Missing"
        val date = args?.date ?: "Date Missing"

        // Set text in the PDFs
        val editTextData = listOf(to,schoolName,schoolAddress,subject,sirMam,sci,absentDate,studentName,clask,rollno,date)
        applicationPamplet.setData(editTextData)

        // Generate the PDFs
        pdfBytes1 = applicationPamplet.generateApplicationPdf(requireContext())
//        pdfBytes2 = applicationPamplet.generatePdf2(requireContext())

        // Load PDFs into the PDF views
        pdfBytes1?.let { bytes ->
            binding.pdfView1.fromBytes(bytes).load()
        } ?: showToast("Failed to generate PDF 1")


        // Handle Save PDF buttons
        binding.pdfBtn1.setOnClickListener {
            pdfBytes1?.let { bytes ->
                if (savePdfToDownloads(requireContext(), bytes, "application_pamplet_1.pdf")) {
                    showToast("PDF 1 saved successfully in Downloads")
                } else {
                    showToast("Failed to save PDF 1")
                }
            } ?: showToast("No PDF 1 to save")
        }

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
