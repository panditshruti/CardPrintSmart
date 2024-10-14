package com.shrutipandit.cardprintsmart.card

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.lifecycle.ViewModel
import java.io.ByteArrayOutputStream

class QuestionCard : ViewModel() {

    fun generatePdf(context: Context, textToDisplay: String): ByteArray {
        val myPdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size in points
        val page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)

        val canvas: Canvas = page.canvas

        // Fill the page with white color
        canvas.drawColor(Color.WHITE)

        // Set up paint for the text
        val paint = Paint().apply {
            color = Color.BLACK // Set text color to black
            textSize = 30f // Set text size (adjust for visibility)
            isFakeBoldText = true // Optional: make the text bold
        }

        // Draw the text "Shruti Pandit" centered on the page
        val text = "Shruti Pandit"
        val textWidth = paint.measureText(text)
        val x = (canvas.width - textWidth) / 2 // Center the text horizontally
        val y = canvas.height / 2 // Center the text vertically
        canvas.drawText(text, x, y.toFloat(), paint)

        // Finish the page
        myPdfDocument.finishPage(page)

        // Write the PDF to a byte array
        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }
}
