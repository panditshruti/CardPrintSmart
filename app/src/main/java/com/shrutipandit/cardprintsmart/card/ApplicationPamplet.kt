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

    fun generatePdf(context: Context): ByteArray {
        val myPdfDocument = PdfDocument()

        // Set the page size to landscape (swap width and height)
        val pageInfo =
            PdfDocument.PageInfo.Builder(842, 595, 1).create() // 842x595 for A4 landscape
        val page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        // Load and draw the background image
        val bgBitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.skpspp
        )
        val scaledBgBitmap = Bitmap.createScaledBitmap(bgBitmap, canvas.width, canvas.height, true)
        canvas.drawBitmap(scaledBgBitmap, 0f, 0f, null)

        // Define Paint for text
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 13.0f // Default text size
        }

        // Define specific positions for each text (x, y)
        val positions = mapOf(
            "to" to Pair(130f, 83f),
            "schoolName" to Pair(95f, 120f),
            "schoolAddress" to Pair(100f, 200f),
            "subject" to Pair(100f, 250f),
            "sirMam" to Pair(100f, 300f),
            "sci" to Pair(100f, 350f),
            "absentDate" to Pair(100f, 400f),
            "studentName" to Pair(100f, 450f),
            "clask" to Pair(100f, 500f),
            "rollno" to Pair(100f, 450f),
            "date" to Pair(100f, 400f)
        )

        // Assign keys to the data list
        val keys = listOf(
            "to",
            "schoolName",
            "schoolAddress",
            "subject",
            "sirMam",
            "sci",
            "absentDate",
            "studentName",
            "clask",
            "rollno",
            "date"
        )

        // Draw text dynamically based on its position
        keys.forEachIndexed { index, key ->
            val position = positions[key]
            val text = data.getOrNull(index)
            if (position != null && text != null) {
                canvas.drawText(text, position.first, position.second, textPaint)
            }
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