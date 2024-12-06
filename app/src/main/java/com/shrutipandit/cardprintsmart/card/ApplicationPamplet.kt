package com.shrutipandit.cardprintsmart.card

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.lifecycle.ViewModel
import com.shrutipandit.cardprintsmart.R
import java.io.ByteArrayOutputStream

class ApplicationPamplet : ViewModel() {

    private var data: MutableList<String> = mutableListOf()

    fun setData(data: List<String>) {
        this.data = data.toMutableList()
    }

    fun generateApplicationPdf(context: Context): ByteArray {
        val myPdfDocument = PdfDocument()

        // Set the page size to A4 (portrait)
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // 595x842 for A4 portrait
        val page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        // Load and draw the background image
//        val bgBitmap = BitmapFactory.decodeResource(
//            context.resources,
//            R.drawable.skpappppamplet2
//        )
//        val scaledBgBitmap = Bitmap.createScaledBitmap(bgBitmap, canvas.width, canvas.height, true)
//        canvas.drawBitmap(scaledBgBitmap, 0f, 0f, null)

        // Define Paint for text
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 13.0f
        }

        // Extract data for the application (ensure data size matches keys)
        val to = data.getOrNull(0) ?: "Recipient Name"
        val schoolName = data.getOrNull(1) ?: "School Name"
        val schoolAddress = data.getOrNull(2) ?: "School Address"
        val subject = data.getOrNull(3) ?: "Subject"
        val sirmam = data.getOrNull(4) ?: "Subject"
        val absentDate = data.getOrNull(6) ?: "Date of Absence"
        val studentName = data.getOrNull(7) ?: "Student Name"
        val rollNo = data.getOrNull(9) ?: "Roll Number"
        val date = data.getOrNull(10) ?: "Current Date"

        // Format the application content
        val applicationText = """
        To,
        The $to
        $schoolName
        $schoolAddress
        Subject: $subject
        
        Respected $sirmam,
        
        I am $studentName a student of your class, humbly request your permission for leave. I was
        unable to attend school on $absentDate due to unforeseen circumstances. My roll
        number is $rollNo. Kindly grant me leave for the mentioned date.
       
        Thank you for your understanding and consideration.
        
        Yours obedient student,
        Name- $studentName
        Date- $date
    """.trimIndent()

        // Define the starting position for the text
        val startX = 50f
        var startY = 100f
        val lineSpacing = 20f

        // Split the text into lines and draw each line
        applicationText.split("\n").forEach { line ->
            canvas.drawText(line, startX, startY, textPaint)
            startY += lineSpacing
        }

        myPdfDocument.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }
    fun generateCollectorApplicationPdf(context: Context, collectorData: Map<String, String>): ByteArray {
        val myPdfDocument = PdfDocument()

        // Set the page size to A4 (portrait)
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // 595x842 for A4 portrait
        val page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        // Define Paint for text
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 13.0f
        }

        // Extract data from the collector map with default values
        val to = collectorData["to"] ?: "Collector Name"
        val department = collectorData["department"] ?: "Department Name"
        val location = collectorData["location"] ?: "Location Name"
        val subject = collectorData["subject"] ?: "Subject of Application"
        val body = collectorData["body"] ?: "Application body content goes here."
        val applicantName = collectorData["applicantName"] ?: "Applicant Name"
        val date = collectorData["date"] ?: "Date of Application"

        // Format the application content dynamically
        val applicationText = """
        To,
        The $to
        $department
        $location

        Subject: $subject

        Respected Sir/Madam,

        $body

        Yours faithfully,
        Name: $applicantName
        Date: $date
    """.trimIndent()

        // Define the starting position for the text
        val startX = 50f
        var startY = 100f
        val lineSpacing = 20f

        // Split the text into lines and draw each line
        applicationText.split("\n").forEach { line ->
            canvas.drawText(line, startX, startY, textPaint)
            startY += lineSpacing
        }

        myPdfDocument.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }


}
