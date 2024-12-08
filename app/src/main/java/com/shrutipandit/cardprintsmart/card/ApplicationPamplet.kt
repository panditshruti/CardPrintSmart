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

    fun generateApplicationPdf(context: Context, schoolData: List<String>): ByteArray {
        val myPdfDocument = PdfDocument()

        // Set the page size to A4 (portrait)
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        var page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // Define Paint for text
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 13.0f
        }

        // Extract data for the application
        val to = schoolData.getOrNull(0) ?: "To Missing"
        val schoolAddress = schoolData.getOrNull(1) ?: "School Address Missing"
        val subject = schoolData.getOrNull(2) ?: "Subject Missing"
        val sirMam = schoolData.getOrNull(3) ?: "Sir/Mam Missing"
        val body = schoolData.getOrNull(4) ?: "Body Missing"
        val applicantName = schoolData.getOrNull(5) ?: "Applicant Name Missing"
        val date = schoolData.getOrNull(6) ?: "Date Missing"

        // Format the application content
        val applicationText = """
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

        // Define the starting position and page dimensions
        val startX = 50f
        var startY = 100f
        val lineSpacing = 20f
        val pageHeight = 842 - 100 // Adjust for margins at the bottom

        // Split the content into lines and handle text wrapping
        val lines = applicationText.split("\n")
        val wrappedLines = mutableListOf<String>()

        val maxWidth = 495f // Adjust for horizontal margins
        for (line in lines) {
            var currentLine = line
            while (textPaint.measureText(currentLine) > maxWidth) {
                val cutIndex = textPaint.breakText(currentLine, true, maxWidth, null)
                wrappedLines.add(currentLine.substring(0, cutIndex))
                currentLine = currentLine.substring(cutIndex)
            }
            wrappedLines.add(currentLine)
        }

        // Draw text, handling page overflow
        for (line in wrappedLines) {
            if (startY + lineSpacing > pageHeight) {
                myPdfDocument.finishPage(page)
                page = myPdfDocument.startPage(pageInfo)
                canvas.drawColor(Color.WHITE) // Clear the canvas for the new page
                startY = 100f // Reset startY for the new page
            }
            canvas.drawText(line, startX, startY, textPaint)
            startY += lineSpacing
        }

        myPdfDocument.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }

//    fun generateCollectorApplicationPdf(context: Context, collectorData: List<String>): ByteArray {
//        val myPdfDocument = PdfDocument()
//
//        // Set the page size to A4 (595x842 for portrait mode)
//        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
//        val page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)
//
//        val canvas = page.canvas
//
//        // Define Paint for text
//        val textPaint = Paint().apply {
//            color = Color.BLACK
//            textSize = 13.0f
//        }
//
//        // Extract data from the map with safe access and default values
//        val recipientName = collectorData["to"] ?: "Recipient Name"
//        val department = collectorData["department"] ?: "Department Name"
//        val location = collectorData["location"] ?: "Location"
//        val subject = collectorData["subject"] ?: "Subject"
//        val body = collectorData["body"] ?: "Application body content goes here."
//        val applicantName = collectorData["applicantName"] ?: "Applicant Name"
//        val date = collectorData["date"] ?: "Date"
//
//        // Format the application content dynamically
//        val applicationText = """
//        To,
//        The $recipientName
//        $department
//        $location
//
//        Subject: $subject
//
//        Respected Sir/Madam,
//
//        $body
//
//        Yours faithfully,
//        Name: $applicantName
//        Date: $date
//    """.trimIndent()
//
//        // Define the starting position for the text
//        val startX = 50f
//        var startY = 100f
//        val lineSpacing = 20f
//
//        // Split the text into lines and draw each line on the canvas
//        applicationText.split("\n").forEach { line ->
//            canvas.drawText(line, startX, startY, textPaint)
//            startY += lineSpacing
//        }
//
//        // Finish the page and close the PDF
//        myPdfDocument.finishPage(page)
//
//        // Convert the PDF to a byte array
//        val outputStream = ByteArrayOutputStream()
//        myPdfDocument.writeTo(outputStream)
//        myPdfDocument.close()
//
//        return outputStream.toByteArray()
//    }
//


}
