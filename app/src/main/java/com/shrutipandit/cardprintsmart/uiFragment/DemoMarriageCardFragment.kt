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
import com.shrutipandit.cardprintsmart.card.MarriageBioData
import com.shrutipandit.cardprintsmart.databinding.FragmentDemoMarriageCardBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class DemoMarriageCardFragment : Fragment(R.layout.fragment_demo_marriage_card) {
    private lateinit var binding: FragmentDemoMarriageCardBinding
    private val marriageBioData = MarriageBioData()
    private var pdfBytes: ByteArray? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDemoMarriageCardBinding.bind(view)

        checkAndRequestPermissions()

        val args = arguments?.let { DemoMarriageCardFragmentArgs.fromBundle(it) }
        val editTextData = args?.editTextData?.split(",")?.toMutableList()

        if (editTextData != null) {
            marriageBioData.setData(editTextData)
            pdfBytes = marriageBioData.generatePdf(requireContext())
            binding.pdfView.fromBytes(pdfBytes).load()
        }

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

        val file = File(downloadsDir, "e_marriage_card.pdf")
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
            put(MediaStore.MediaColumns.DISPLAY_NAME, "e_marriage_card.pdf")
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
