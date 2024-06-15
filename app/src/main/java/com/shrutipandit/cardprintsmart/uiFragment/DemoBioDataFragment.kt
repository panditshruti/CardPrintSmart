package com.shrutipandit.cardprintsmart.uiFragment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.card.BioData
import com.shrutipandit.cardprintsmart.databinding.FragmentDemoBioDataBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager

class DemoBioDataFragment : Fragment(R.layout.fragment_demo_bio_data) {
    private lateinit var binding: FragmentDemoBioDataBinding
    private val bioData = BioData()
    private var pdfBytes: ByteArray? = null
    private var selectedImageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDemoBioDataBinding.bind(view)

        checkAndRequestPermissions()

        val args = arguments?.let { DemoBioDataFragmentArgs.fromBundle(it) }
        val editTextData = args?.editTextData?.split(",")?.toMutableList()
        val imageUriString = args?.image
        selectedImageUri = if (imageUriString.isNullOrEmpty()) null else Uri.parse(imageUriString)

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
            selectedImageUri?.let { uri ->
                if (savePdfToDownloads(requireContext(), pdfBytes!!)) {
                    showToast("PDF saved successfully in Downloads")
                } else {
                    showToast("Failed to save PDF")
                }
            } ?: showToast("No image selected")
        }
    }

    private fun savePdfToDownloads(context: Context, pdfBytes: ByteArray): Boolean {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDir.exists() && !downloadsDir.mkdirs()) {
            return false
        }

        val file = File(downloadsDir, "e_bioData_card.pdf")
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

    private fun checkAndRequestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permissionsToRequest =
            permissions.filter { ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toTypedArray(),
                0
            )
        }
    }
}
