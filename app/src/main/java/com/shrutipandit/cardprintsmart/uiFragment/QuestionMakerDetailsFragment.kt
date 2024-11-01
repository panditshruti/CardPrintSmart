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

    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString("title")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuestionMakerDetailsBinding.bind(view)
        checkAndRequestPermissions()

        // Add the title to the question list as the first entry
        title?.let { t -> questionList.add(QuestionData(t, "", "")) }

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
        val textWidth = pageWidth - (2 * margin)

        // Paint setup
        val paint = Paint().apply { textSize = 12f }
        val textPaint = TextPaint(paint)

        val headingPaint = Paint().apply {
            isFakeBoldText = true
            textSize = 14f
        }
        val headingTextPaint = TextPaint(headingPaint)

        var pageNumber = 1
        var yPos = margin + 30f // Start position for content
        var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        // Draw title only once at the beginning
        if (questionList.isNotEmpty()) {
            val titleText = questionList[0].heading
            if (titleText.isNotEmpty()) {
                val titleWidth = headingTextPaint.measureText(titleText) // Measure the title width
                val titleX = (pageWidth - titleWidth) / 2 // Center the title horizontally
                // Draw the title at a specific Y position
                canvas.drawText(titleText, titleX, yPos, headingTextPaint) // Draw the title
                yPos += headingTextPaint.fontMetrics.bottom - headingTextPaint.fontMetrics.top + 20f // Increase spacing after title
            }
        }

        // Loop through each question starting from index 1 (to skip the title)
        for (index in 1 until questionList.size) {
            val questionData = questionList[index]

            // Function to draw wrapped text
            fun drawWrappedText(text: String, yPos: Float, additionalGap: Float = 0f, paint: TextPaint): Float {
                val staticLayout = StaticLayout.Builder.obtain(text, 0, text.length, paint, textWidth.toInt())
                    .setLineSpacing(0f, 1f)
                    .setAlignment(android.text.Layout.Alignment.ALIGN_NORMAL)
                    .build()

                var currentYPos = yPos
                for (i in 0 until staticLayout.lineCount) {
                    val lineBottom = staticLayout.getLineBottom(i)
                    if (currentYPos + lineBottom > pageHeight - margin) {
                        pdfDocument.finishPage(page)
                        pageNumber++
                        pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
                        page = pdfDocument.startPage(pageInfo)
                        canvas = page.canvas
                        currentYPos = margin + 30f // Reset Y position for new page
                    }
                    canvas.drawText(
                        text.substring(staticLayout.getLineStart(i), staticLayout.getLineEnd(i)),
                        margin,
                        currentYPos,
                        paint
                    )
                    currentYPos += paint.fontMetrics.bottom - paint.fontMetrics.top
                }
                return currentYPos + additionalGap
            }

            // Center the heading for each question
            val headingText = questionData.heading
            if (headingText.isNotEmpty()) {
                val headingWidth = headingTextPaint.measureText(headingText) // Measure the heading width
                val headingX = (pageWidth - headingWidth) / 2 // Center the heading horizontally
                canvas.drawText(headingText, headingX, yPos, headingTextPaint) // Draw the heading
                yPos += headingTextPaint.fontMetrics.bottom - headingTextPaint.fontMetrics.top + 10f // Increase spacing after heading
            }

            // Add the question number based on its position in the list
            val questionText = "Q${index}: ${questionData.question}?"
            yPos = drawWrappedText(questionText, yPos, 5f, textPaint)

            // Draw options (if any)
            yPos = drawWrappedText(questionData.option, yPos, 5f, textPaint)
        }

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
            val heading = etHeading.text.toString().trim() // Trim to avoid leading/trailing spaces
            val question = etQuestion.text.toString().trim()
            val option = etOption.text.toString().trim()

            if (question.isNotEmpty() && option.isNotEmpty()) {
                // Add the new question to the list
                questionList.add(QuestionData(heading, question, option)) // Maintain heading with the question
                updatePdfView() // Update PDF view with the new data
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
        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

        return if (uri != null) {
            resolver.openOutputStream(uri).use { outputStream ->
                outputStream?.write(pdfBytes)
            } != null
        } else {
            false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }
    }
}
