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

}
//
//
//
//    fun generatePdf2(context: Context): ByteArray {
//        val myPdfDocument = PdfDocument()
//
//        // Set the page size to landscape (swap width and height)
//        val pageInfo =
//            PdfDocument.PageInfo.Builder(842, 595, 1).create() // 842x595 for A4 landscape
//        val page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)
//
//        val canvas = page.canvas
//
//        // Load and draw the background image
//        val bgBitmap = BitmapFactory.decodeResource(
//            context.resources,
//            R.drawable.marriagep2
//        ) // Replace 'maragepampletcard' with your actual drawable name
//        val scaledBgBitmap = Bitmap.createScaledBitmap(bgBitmap, canvas.width, canvas.height, true)
//        canvas.drawBitmap(scaledBgBitmap, 0f, 0f, null)
//
//        // Define Paint for Dulha
//        val dulhaPaint = Paint().apply {
//            color = Color.BLACK
//            textSize = 50.5f // Text size for Dulha
//        }
//
//        // Define Paint for Dulhan
//        val dulhanPaint = Paint().apply {
//            color = Color.BLACK
//            textSize = 50.0f // Text size for Dulhan
//        }
//
//        // Static positions for each item
//        val dulhaPositionX = 170f
//        val dulhaPositionY = 230f
//
//        val dulhanPositionX = 570f
//        val dulhanPositionY = 400f
//
//
//        // Draw Dulha
//        val dulhaText = data.getOrNull(0)
//        if (dulhaText != null) {
//            canvas.drawText(dulhaText, dulhaPositionX, dulhaPositionY, dulhaPaint)
//        }
//
//        // Draw Dulhan
//        val dulhanText = data.getOrNull(1)
//        if (dulhanText != null) {
//            canvas.drawText(dulhanText, dulhanPositionX, dulhanPositionY, dulhanPaint)
//        }
//
//        myPdfDocument.finishPage(page)
//
//        val outputStream = ByteArrayOutputStream()
//        myPdfDocument.writeTo(outputStream)
//        myPdfDocument.close()
//
//        return outputStream.toByteArray()
//    }
//
//
//}