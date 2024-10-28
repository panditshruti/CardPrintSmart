package com.shrutipandit.cardprintsmart.uiFragment

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentQuestionMakerDetailsBinding
import com.shrutipandit.cardprintsmart.db.QuestionData
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class QuestionMakerDetailsFragment : Fragment(R.layout.fragment_question_maker_details) {

    private lateinit var binding: FragmentQuestionMakerDetailsBinding
    private var pdfBytes: ByteArray? = null
    private val questionList = mutableListOf<QuestionData>()

    // Variables to hold the passed arguments
    private var title: String? = null
    private var description: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString("title")
            description = it.getString("description")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuestionMakerDetailsBinding.bind(view)
        checkAndRequestPermissions()

        // If title and description are not null, create a QuestionData object
        title?.let { t ->
            description?.let { d ->
                questionList.add(QuestionData(t, d, ""))
            }
        }

        updatePdfView()

        binding.pdfBtn.setOnClickListener {
            pdfBytes?.let { bytes ->
                if (savePdfToDownloads(requireContext(), bytes)) {
                    showToast("PDF saved successfully in Downloads")
                } else {
                    showToast("Failed to save PDF")
                }
            } ?: showToast("No PDF to save")
        }

        binding.addQuestionBtn.setOnClickListener {
            showAddQuestionDialog()
        }
    }

    private fun generatePdf(): ByteArray {
        val pdfDocument = PdfDocument()
        val pageWidth = 300
        val pageHeight = 600
        val margin = 10f
        val textWidth = pageWidth - (2 * margin) // Available width for text after margins

        // Paint for normal text (question and option)
        val paint = Paint().apply {
            textSize = 12f // Set the text size for questions and options
        }

        val textPaint = TextPaint(paint)

        // Paint for heading (bold and larger text)
        val headingPaint = Paint().apply {
            isFakeBoldText = true // Make text bold
            textSize = 14f // Set a larger text size for heading
        }

        val headingTextPaint = TextPaint(headingPaint)

        var pageNumber = 1
        var yPos = margin + 30f // Initial Y position with a top margin
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        // Initialize a counter for question numbering
        var questionCounter = 1

        // Loop through each question and draw on the page
        for ((index, questionData) in questionList.withIndex()) {
            // Function to draw wrapped text with minimum line spacing
            fun drawWrappedText(text: String, yPos: Float, additionalGap: Float = 0f, paint: TextPaint): Float {
                val staticLayout = StaticLayout.Builder.obtain(text, 0, text.length, paint, textWidth.toInt())
                    .setLineSpacing(0f, 1f) // Keep default line spacing
                    .setAlignment(android.text.Layout.Alignment.ALIGN_NORMAL)
                    .build()

                var currentYPos = yPos

                for (i in 0 until staticLayout.lineCount) {
                    val lineBottom = staticLayout.getLineBottom(i)

                    // Check if the current line fits in the remaining space on the page
                    if (currentYPos + lineBottom > pageHeight - margin) {
                        // Finish the current page and start a new one
                        pdfDocument.finishPage(page)
                        pageNumber++
                        pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
                        page = pdfDocument.startPage(pageInfo)
                        canvas = page.canvas

                        currentYPos = margin + 30f // Reset Y position for the new page
                    }

                    // Draw the current line on the page
                    canvas.drawText(
                        text.substring(staticLayout.getLineStart(i), staticLayout.getLineEnd(i)),
                        margin,
                        currentYPos,
                        paint
                    )

                    // Update currentYPos based on the height of the line
                    currentYPos += paint.fontMetrics.bottom - paint.fontMetrics.top // Use font metrics to calculate line height
                }

                return currentYPos + additionalGap // Return updated Y position with additional gap
            }

            // Draw the heading centered if it exists
            val headingText = questionData.heading
            if (headingText.isNotEmpty()) {
                val headingWidth = headingTextPaint.measureText(headingText)
                val headingX = (pageWidth - headingWidth) / 2 // Calculate X position for centering

                // Draw heading at the calculated X position
                canvas.drawText(headingText, headingX, yPos, headingTextPaint)

                // Update yPos after drawing the heading
                yPos += headingTextPaint.fontMetrics.bottom - headingTextPaint.fontMetrics.top + 5f // Adjust Y position for the next section
            } else {
                // If no heading, don't increase yPos, but still leave a small gap
                yPos += 5f // Small gap when there's no heading
            }

            // Add the question number dynamically
            val questionText = if (questionData.question.trimEnd().endsWith("?")) {
                "Q$questionCounter: ${questionData.question}"
            } else {
                "Q$questionCounter: ${questionData.question}?"
            }
            yPos = drawWrappedText(questionText, yPos, 5f, textPaint) // Use textPaint for question text with minimal gap

            yPos = drawWrappedText(questionData.option, yPos, 5f, textPaint) // Use textPaint for option text with minimal gap

            // Increment the question counter
            questionCounter++
        }

        // Finish the last page
        pdfDocument.finishPage(page)

        // Write the document to a byte array
        val stream = ByteArrayOutputStream()
        try {
            pdfDocument.writeTo(stream)
        } finally {
            pdfDocument.close()
        }

        return stream.toByteArray()
    }

    // Show the dialog to add a new question
    private fun showAddQuestionDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_question, null)

        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Add Question")
            .create()

        val etHeading = dialogView.findViewById<EditText>(R.id.etHeading)
        val etQuestion = dialogView.findViewById<EditText>(R.id.etQuestion)
        val etOption = dialogView.findViewById<EditText>(R.id.etOption)
        val btnSubmit = dialogView.findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val heading = etHeading.text.toString()
            val question = etQuestion.text.toString()
            val option = etOption.text.toString()

            if (question.isNotEmpty() && option.isNotEmpty()) {
                // Add the new question to the list
                questionList.add(QuestionData(heading, question, option))
                updatePdfView()
                dialog.dismiss()
            } else {
                showToast("Please enter all fields")
            }
        }

        dialog.show()
    }

    // Update the PDF view with the current list of questions
    private fun updatePdfView() {
        pdfBytes = generatePdf()
        pdfBytes?.let { bytes ->
            binding.pdfView.fromBytes(bytes).load()
        } ?: showToast("Failed to generate PDF")
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
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("Permission granted")
            } else {
                showToast("Permission denied")
            }
        }
    }
}
