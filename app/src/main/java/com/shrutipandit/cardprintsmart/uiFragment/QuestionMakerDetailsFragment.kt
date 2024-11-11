package com.shrutipandit.cardprintsmart.uiFragment

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.shrutipandit.cardprintsmart.AppDatabase
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentQuestionMakerDetailsBinding
import com.shrutipandit.cardprintsmart.db.PageContent
import com.shrutipandit.cardprintsmart.db.Question
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class QuestionMakerDetailsFragment : Fragment(R.layout.fragment_question_maker_details) {

    private lateinit var binding: FragmentQuestionMakerDetailsBinding
    private var pdfBytes: ByteArray? = null
    private val questionList = mutableListOf<Question>()
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

        val args = QuestionMakerDetailsFragmentArgs.fromBundle(requireArguments())
        val title = args.title
        val description = args.description

        checkAndRequestPermissions()
        loadQuestionsFromDatabase()

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

    private fun loadQuestionsFromDatabase() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val pageContents = db.pageContentDao().getAllPageContents()

            questionList.clear()
            pageContents.forEach { content ->
                questionList.addAll(content.question) // Add all questions from each PageContent
            }
            updatePdfView()
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

        // Heading Paint setup for centering and bolding
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

        // Title text centered at the top of the page
        val titleText = title ?: "Question List" // Default title if null
        val titleWidth = headingTextPaint.measureText(titleText)
        val titleX = (pageWidth - titleWidth) / 2 // Center the title horizontally
        canvas.drawText(titleText, titleX, yPos, headingTextPaint)

        // Update yPos for the next content, leaving space after title
        yPos += headingTextPaint.fontMetrics.bottom - headingTextPaint.fontMetrics.top + 20f // Added space after title

        // Define a minimal gap between questions
        val questionGap = 5f  // Reduce this value to minimize gap

        // Loop through each question
        questionList.forEachIndexed { index, questionData ->
            fun drawWrappedText(text: String, yPos: Float, additionalGap: Float = 0f, paint: TextPaint): Float {
                val staticLayout = StaticLayout.Builder.obtain(text, 0, text.length, paint, textWidth.toInt())
                    .setLineSpacing(0f, 1f)  // No additional line spacing
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

            // Draw the question heading and the question itself
            val headingText = questionData.text // This should ideally be the heading
            val questionText = "Q${index + 1}: ${questionData.answer}?" // This should be the main question

            // Center the heading text on the page
            val headingWidth = headingTextPaint.measureText(headingText) // Measure the width of the heading
            val headingX = (pageWidth - headingWidth) / 2 // Calculate the X position to center the heading
            canvas.drawText(headingText, headingX, yPos, headingTextPaint)
            yPos = drawWrappedText("", yPos, 0f, headingTextPaint)
            yPos = drawWrappedText(questionText, yPos, 0f, textPaint)

            // Draw options if any
            yPos = drawWrappedText("Options: ${questionData.options}", yPos, 0f, textPaint)

            // Add a minimal gap between questions
            yPos += questionGap
        }

        pdfDocument.finishPage(page)

        val stream = ByteArrayOutputStream()
        try {
            pdfDocument.writeTo(stream)
        } finally {
            pdfDocument.close()
        }
        return stream.toByteArray()
    }

    private fun showAddQuestionDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_question, null)
        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Add Question")
            .create()

        val etheading = dialogView.findViewById<EditText>(R.id.etHeading)
        val etquestion = dialogView.findViewById<EditText>(R.id.etQuestion)
        val etOptions = dialogView.findViewById<EditText>(R.id.etOption)
        val btnSubmit = dialogView.findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val headingtext = etheading.text.toString().trim()
            val questiontext = etquestion.text.toString().trim()
            val options = etOptions.text.toString().trim()

            if (questiontext.isNotEmpty() && options.isNotEmpty()) {
                val newQuestion = Question(text = headingtext, answer = questiontext, options = options)

                val newPageContent = PageContent(question = listOf(newQuestion))

                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(requireContext())
                    db.pageContentDao().insertPageContent(newPageContent)
                    questionList.add(newQuestion)
                    updatePdfView()
                }
                dialog.dismiss()
            } else {
                showToast("Please enter all fields")
            }
        }

        dialog.show()
    }

    private fun updatePdfView() {
        pdfBytes = generatePdf()
        pdfBytes?.let { bytes ->
            val tempFile = File(requireContext().cacheDir, "temp_pdf_file.pdf")
            tempFile.writeBytes(bytes)
            binding.pdfView.fromFile(tempFile).load()
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
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, "e_question_card.pdf")
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        return uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                outputStream.write(pdfBytes)
                true
            }
        } ?: false
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun checkAndRequestPermissions() {
        // Permission handling logic here
    }
}
