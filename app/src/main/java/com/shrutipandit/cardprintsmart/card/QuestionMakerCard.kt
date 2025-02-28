package com.shrutipandit.cardprintsmart.card

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import java.io.ByteArrayOutputStream

class QuestionMakerCard {

    private var data: MutableList<String> = mutableListOf()

    fun setData(data: MutableList<String>) {
        this.data = data
    }

    fun generatePdf(context: Context, bitmap: Bitmap?): ByteArray {
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

        // Draw the name "Shruti Pandit" in the center
        val namePaint = Paint().apply {
            color = Color.BLACK
            isFakeBoldText = true
            textSize = 40.0f
        }
        val nameText = "Shruti Pandit"
        val textWidth = namePaint.measureText(nameText)
        val nameStartX = (canvas.width - textWidth) / 2
        val nameStartY = (canvas.height / 2).toFloat()
        canvas.drawText(nameText, nameStartX, nameStartY, namePaint)

        myPdfDocument.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }

    private fun drawTextList(canvas: Canvas, labelList: List<String>, dataList: List<String>, x: Float, y: Int, paint: Paint) {
        val lineHeight = 26
        val maxWidth = canvas.width - 2 * (x + 20)
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
}
