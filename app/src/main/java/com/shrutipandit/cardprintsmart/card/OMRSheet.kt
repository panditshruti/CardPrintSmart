package com.shrutipandit.cardprintsmart.card

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.ByteArrayOutputStream

class OMRSheet {

    fun generatePdf(context: Context, numberOfQuestions: Int, paperSize: String): ByteArray {
        val pageInfo = when (paperSize) {
            "A3" -> PdfDocument.PageInfo.Builder(842, 1191, 1).create() // A3 size
            "A5" -> PdfDocument.PageInfo.Builder(420, 595, 1).create() // A5 size
            else -> PdfDocument.PageInfo.Builder(595, 842, 1).create() // Default A4 size
        }
        val myPdfDocument = PdfDocument()
        val page = myPdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 14f
        }

        // Draw the questions on the PDF
        for (i in 1..numberOfQuestions) {
            val yPos = 50 + i * 30
            canvas.drawText("Question $i:", 50f, yPos.toFloat(), textPaint)
            // Draw bubbles
            drawBubbles(canvas, yPos)
        }

        myPdfDocument.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }

    private fun drawBubbles(canvas: Canvas, yPos: Int) {
        val bubblePaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
        }

        val options = listOf("A", "B", "C", "D")
        for ((index, option) in options.withIndex()) {
            val xPos = 150 + index * 50
            canvas.drawCircle(xPos.toFloat(), yPos.toFloat() - 10, 15f, bubblePaint)
            canvas.drawText(option, xPos.toFloat() - 5, yPos.toFloat() + 20, bubblePaint)
        }
    }
}
