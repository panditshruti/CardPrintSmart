package com.shrutipandit.cardprintsmart.card

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.lifecycle.ViewModel
import java.io.ByteArrayOutputStream

class ApplicationPamplet : ViewModel() {

    private var data: MutableList<String> = mutableListOf()

    fun setData(data: List<String>) {
        this.data = data.toMutableList()
    }

    fun generateApplicationPdfEnglish(
        context: Context,
        schoolData: List<String>,
        inputLanguage: String = "en" // Default input language
    ): ByteArray {
        val myPdfDocument = PdfDocument()

        // Page size: A4 (portrait)
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        var page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        // Text Paint
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 13.0f
        }

        // Extract Data
        val to = schoolData.getOrNull(0) ?: "To Missing"
        val schoolAddress = schoolData.getOrNull(1) ?: "School Address Missing"
        val subject = schoolData.getOrNull(2) ?: "Subject Missing"
        val sirMam = schoolData.getOrNull(3) ?: "Sir/Mam Missing"
        val body = schoolData.getOrNull(4) ?: "Body Missing"
        val applicantName = schoolData.getOrNull(5) ?: "Applicant Name Missing"
        val date = schoolData.getOrNull(6) ?: "Date Missing"

        val englishText = """
        To,
        The $to
        $schoolAddress
        Subject: $subject

        Respected $sirMam,

        $body

        Yours faithfully,
        Name: $applicantName
        Date: $date
    """.trimIndent()

        // Constants for positioning
        val startX = 50f
        var startY = 100f
        val lineSpacing = 20f
        val pageHeight = 842 - 100 // Leave bottom margin
        val pageWidth = 595 - 100 // Leave side margins

        // Split English text into lines
        val lines = englishText.split("\n")

        // Process each line and ensure correct formatting
        for (line in lines) {
            // Check if the line fits in the remaining width or wrap it
            val words = line.split(" ")
            var currentLine = ""

            for (word in words) {
                val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
                val textWidth = textPaint.measureText(testLine)

                if (textWidth > pageWidth) { // Line exceeds page width, wrap it
                    if (startY + lineSpacing > pageHeight) {
                        // Finish the current page and create a new one
                        myPdfDocument.finishPage(page)
                        page = myPdfDocument.startPage(pageInfo)
                        canvas = page.canvas
                        startY = 100f // Reset start position for new page
                    }
                    // Draw the current line
                    canvas.drawText(currentLine, startX, startY, textPaint)
                    currentLine = word // Start a new line with the current word
                    startY += lineSpacing
                } else {
                    currentLine = testLine
                }
            }

            // Draw any remaining text in the current line
            if (currentLine.isNotEmpty()) {
                if (startY + lineSpacing > pageHeight) {
                    // Finish the current page and create a new one
                    myPdfDocument.finishPage(page)
                    page = myPdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    startY = 100f // Reset start position for new page
                }
                canvas.drawText(currentLine, startX, startY, textPaint)
                startY += lineSpacing
            }

            // Add additional spacing for empty lines (if any)
            if (line.isBlank()) {
                startY += lineSpacing
            }
        }

        myPdfDocument.finishPage(page)

        // Write PDF to output
        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }


    fun generateApplicationPdfHindi(
        context: Context,
        schoolData: List<String>,
        inputLanguage: String = "en" // Default input language
    ): ByteArray {
        val myPdfDocument = PdfDocument()

        // Page size: A4 (portrait)
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        var page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        // Text Paint
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 13.0f
        }

        // Extract Data
        val to = schoolData.getOrNull(0) ?: "To Missing"
        val schoolAddress = schoolData.getOrNull(1) ?: "School Address Missing"
        val subject = schoolData.getOrNull(2) ?: "Subject Missing"
        val sirMam = schoolData.getOrNull(3) ?: "Sir/Mam Missing"
        val body = schoolData.getOrNull(4) ?: "Body Missing"
        val applicantName = schoolData.getOrNull(5) ?: "Applicant Name Missing"
        val date = schoolData.getOrNull(6) ?: "Date Missing"

        val englishText = """
        सेवा में,
        श्रीमान $to
        $schoolAddress
        विषय: $subject

        मान्यवर $sirMam,

        $body

        आपका/आपकि विश्वासी,
        नाम: $applicantName
        दिनांक: $date
        """.trimIndent()

        // Constants for positioning
        val startX = 50f
        var startY = 100f
        val lineSpacing = 20f
        val pageHeight = 842 - 100 // Leave bottom margin
        val pageWidth = 595 - 100 // Leave side margins

        // Split English text into lines
        val lines = englishText.split("\n")

        // Process each line and ensure correct formatting
        for (line in lines) {
            // Check if the line fits in the remaining width or wrap it
            val words = line.split(" ")
            var currentLine = ""

            for (word in words) {
                val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
                val textWidth = textPaint.measureText(testLine)

                if (textWidth > pageWidth) { // Line exceeds page width, wrap it
                    if (startY + lineSpacing > pageHeight) {
                        // Finish the current page and create a new one
                        myPdfDocument.finishPage(page)
                        page = myPdfDocument.startPage(pageInfo)
                        canvas = page.canvas
                        startY = 100f // Reset start position for new page
                    }
                    // Draw the current line
                    canvas.drawText(currentLine, startX, startY, textPaint)
                    currentLine = word // Start a new line with the current word
                    startY += lineSpacing
                } else {
                    currentLine = testLine
                }
            }

            // Draw any remaining text in the current line
            if (currentLine.isNotEmpty()) {
                if (startY + lineSpacing > pageHeight) {
                    // Finish the current page and create a new one
                    myPdfDocument.finishPage(page)
                    page = myPdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    startY = 100f // Reset start position for new page
                }
                canvas.drawText(currentLine, startX, startY, textPaint)
                startY += lineSpacing
            }

            // Add additional spacing for empty lines (if any)
            if (line.isBlank()) {
                startY += lineSpacing
            }
        }

        myPdfDocument.finishPage(page)

        // Write PDF to output
        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }


}
