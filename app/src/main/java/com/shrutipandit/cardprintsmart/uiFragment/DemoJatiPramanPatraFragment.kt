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
import com.shrutipandit.cardprintsmart.card.JatiPramanPatra
import com.shrutipandit.cardprintsmart.databinding.FragmentDemoJatiPramanPatraBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DemoJatiPramanPatraFragment : Fragment(R.layout.fragment_demo_jati_praman_patra) {

   private lateinit var binding: FragmentDemoJatiPramanPatraBinding
   private val jatiPramPatra = JatiPramanPatra()
   private var pdfBytes1: ByteArray? = null

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      binding = FragmentDemoJatiPramanPatraBinding.bind(view)

      // Check and request permissions
      checkAndRequestPermissions()

      // Extract arguments passed from the previous fragment
      val args = arguments?.let { DemoJatiPramanPatraFragmentArgs.fromBundle(it) }
      val editTextData = listOf(
         args?.formNumber ?: "Form Number Missing",
         args?.district ?: "District Missing",
         args?.anumandal ?: "Anumandal Missing",
         args?.circle ?: "Circle Missing",
         args?.pramanPatraNnumber ?: "Certificate Number Missing",
         args?.date ?: "Date Missing",
         args?.name ?: "Name Missing",
         args?.hindiName ?: "Name Missing",
         args?.fatherName ?: "Father Name Missing",
         args?.hindiFatherName ?: "Father Name Missing",
         args?.motherName ?: "Mother Name Missing",
         args?.hindiMotherName ?: "Mother Name Missing",
         args?.village ?: "Village Missing",
         args?.postOffice ?: "Post Office Missing",
         args?.policeStation ?: "Police Station Missing",
         args?.prakhanad ?: "Prakhand Missing",
         args?.caste ?: "Caste Missing",
         args?.anusuchi ?: "Anusuchi Missing",
         args?.anukramank ?: "Anukramank Missing",
         args?.digitallySignedBy ?: "Signed By Missing",
         args?.dateandTime ?: "Date and Time Missing"
      )

      // Generate the PDF
      pdfBytes1 = jatiPramPatra.generateCasteCertificatePdf(requireContext(), editTextData)

      // Load PDF into the PDF view
      pdfBytes1?.let { bytes ->
         binding.pdfView.fromBytes(bytes).load()
      } ?: showToast("Failed to generate PDF")

      // Handle Save PDF button
      binding.pdfBtn1.setOnClickListener {
         pdfBytes1?.let { bytes ->
            if (savePdfToDownloads(requireContext(), bytes, "jati_praman_patra.pdf")) {
               showToast("PDF saved successfully in Downloads")
            } else {
               showToast("Failed to save PDF")
            }
         } ?: showToast("No PDF to save")
      }
   }

   private fun savePdfToDownloads(
      context: Context,
      pdfBytes: ByteArray,
      fileName: String
   ): Boolean {
      return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
         savePdfToDownloadsScoped(context, pdfBytes, fileName)
      } else {
         savePdfToDownloadsLegacy(context, pdfBytes, fileName)
      }
   }

   private fun savePdfToDownloadsLegacy(
      context: Context,
      pdfBytes: ByteArray,
      fileName: String
   ): Boolean {
      val downloadsDir =
         Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
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
   private fun savePdfToDownloadsScoped(
      context: Context,
      pdfBytes: ByteArray,
      fileName: String
   ): Boolean {
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
            }
            true
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
      val permissions = mutableListOf(Manifest.permission.READ_EXTERNAL_STORAGE)

      // Add WRITE_EXTERNAL_STORAGE permission for versions below Android Q
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
         permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
      }

      val permissionsToRequest = permissions.filter {
         ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
      }

      if (permissionsToRequest.isNotEmpty()) {
         ActivityCompat.requestPermissions(
            requireActivity(),
            permissionsToRequest.toTypedArray(),
            REQUEST_CODE_PERMISSIONS
         )
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