package com.shrutipandit.cardprintsmart.card

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import androidx.lifecycle.ViewModel
import java.io.ByteArrayOutputStream

class ExamCopy : ViewModel() {
    private var data: MutableList<String> = mutableListOf()
    private var numberOfLines: Int = 0 // User-defined number of lines

    fun setData(data: MutableList<String>) {
        this.data = data
    }

    // Set the number of lines based on user input
    fun setNumberOfLines(lines: Int) {
        numberOfLines = lines
    }

    fun generatePdf(context: Context, bitmap: Bitmap): ByteArray {
        val myPdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        // Draw the border
        val borderPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }
        val borderMargin = 20
        val borderRect = Rect(borderMargin, borderMargin, canvas.width - borderMargin, canvas.height - borderMargin)
        canvas.drawRect(borderRect, borderPaint)

        // Draw the heading at the top
        val headingPaint = Paint().apply {
            color = Color.RED
            isFakeBoldText = true
            textSize = 30.0f
        }
        val headingText = "Exam Copy"
        val textWidth = headingPaint.measureText(headingText)
        val headingStartX = (canvas.width - textWidth) / 2
        val headingStartY = borderMargin + 30
        canvas.drawText(headingText, headingStartX, headingStartY.toFloat(), headingPaint)

        // Call drawHeader to include the name, roll no, etc.
        val headerStartY = headingStartY + 50 // Adjust to place the header below the main title
        val currentY = drawHeader(canvas, headerStartY.toInt())

        // Position for additional personal information (below header)
        val personalStartY = currentY.toInt() + 20

        val personalInfoArrayList = arrayListOf(
            "School Name   :",
            "Total Marks    :"
        )

        drawTextList(
            canvas,
            personalInfoArrayList,
            data.subList(0, personalInfoArrayList.size),
            borderMargin.toFloat() + 10,
            personalStartY,
            Paint().apply {
                color = Color.BLACK
                textSize = 20.5f
            }
        )

        // Draw the image (e.g., passport photo)
        val imageMargin = 10
        val imagePaint = Paint()
        val passportWidth = 132 - 2 * imageMargin
        val passportHeight = 170 - 2 * imageMargin
        val imageLeft = canvas.width - borderMargin - passportWidth - 2 * imageMargin
        val imageTop = personalStartY + imageMargin
        val imageRect = Rect(imageLeft, imageTop, imageLeft + passportWidth, imageTop + passportHeight)
        canvas.drawBitmap(bitmap, null, imageRect, imagePaint)

        // Draw blank lines (like for an answer sheet)
        val lineStartY = personalStartY + personalInfoArrayList.size * 30 + 50
        drawBlankLines(canvas, lineStartY, numberOfLines, borderMargin.toFloat(), canvas.width.toFloat() - borderMargin)

        // Draw signature
        val signatureText = "Signature"
        val signaturePaint = Paint().apply {
            color = Color.BLACK
            isFakeBoldText = true
            textSize = 20.0f
        }
        val signatureStartX = canvas.width - borderMargin - 100f
        val signatureStartY = canvas.height - borderMargin.toFloat() - 60
        canvas.drawText(signatureText, signatureStartX, signatureStartY, signaturePaint)

        myPdfDocument.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }

    private fun drawHeader(canvas: Canvas, padding: Int): Float {
        val headerPaint = Paint().apply {
            color = Color.BLACK
            textSize = 18f
            isAntiAlias = true
        }

        var currentY = padding + 40f // Starting Y position for the header details

        // Draw Name and Roll No
        canvas.drawText("Name: ____________________", padding.toFloat(), currentY, headerPaint)
        canvas.drawText("Roll No: ____________________", (canvas.width / 2 + padding).toFloat(), currentY, headerPaint)
        currentY += 30f // Move to the next row

        // Draw Class and Mobile
        canvas.drawText("Class: ____________________", padding.toFloat(), currentY, headerPaint)
        canvas.drawText("Mobile: ____________________", (canvas.width / 2 + padding).toFloat(), currentY, headerPaint)
        currentY += 30f // Move to the next row

        // Draw Date and Subject
        canvas.drawText("Date: ____________________", padding.toFloat(), currentY, headerPaint)
        canvas.drawText("Sub: ____________________", (canvas.width / 2 + padding).toFloat(), currentY, headerPaint)
        currentY += 30f // Move to the next row

        // Return the current Y position for further drawing
        return currentY
    }

    private fun drawTextList(canvas: Canvas, labelList: List<String>, dataList: List<String>, x: Float, y: Int, paint: Paint) {
        val lineHeight = 26
        val maxWidth = canvas.width - 2 * (x + 20) // Adjust for margins
        var yPoint = y

        for (i in labelList.indices) {
            val label = labelList[i]
            val data = dataList.getOrNull(i)?.replace(";", ",") ?: ""
            val text = "$label $data"
            val words = text.split(" ")

            var line = ""
            for (word in words) {
                val testLine = if (line.isEmpty()) word else "$line $word"
                val textWidth = paint.measureText(testLine)
                if (textWidth < maxWidth) {
                    line = testLine
                } else {
                    canvas.drawText(line, x, yPoint.toFloat(), paint)
                    yPoint += lineHeight
                    line = word
                }
            }
            canvas.drawText(line, x, yPoint.toFloat(), paint)
            yPoint += lineHeight
        }
    }

    // Function to draw blank lines (like a copy sheet)
    private fun drawBlankLines(canvas: Canvas, startY: Int, numberOfLines: Int, startX: Float, endX: Float) {
        val lineHeight = 30f // Height between each line
        val linePaint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 1.5f
        }

        var currentY = startY.toFloat()

        // Continue drawing lines from top to bottom until the number of lines or page ends
        for (i in 0 until numberOfLines) {
            // Draw a horizontal line from startX to endX
            canvas.drawLine(startX, currentY, endX, currentY, linePaint)
            currentY += lineHeight
        }
    }
}
