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

class IDCard :ViewModel(){
        private var data: MutableList<String> = mutableListOf()

        fun setData(data: MutableList<String>) {
            this.data = data
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

            // Draw the image at the top
            val imagePaint = Paint()
            val imageRect = Rect(borderMargin, borderMargin, canvas.width - borderMargin, 300) // Adjust height as needed
            canvas.drawBitmap(bitmap, null, imageRect, imagePaint)

            val textPaint = Paint().apply {
                color = Color.BLACK
                textSize = 20.5f
            }

            val headingPaint = Paint().apply {
                color = Color.RED
                isFakeBoldText = true
                textSize = 30.0f
            }

            val headingText = "BIO DATA"
            val textWidth = headingPaint.measureText(headingText)
            val headingStartX = (canvas.width - textWidth) / 2
            val headingStartY = imageRect.bottom + 30
            canvas.drawText(headingText, headingStartX, headingStartY.toFloat(), headingPaint)

            val marriagePersonalInfoArrayList = arrayListOf(
                "Name    :",
                "Father's Name    :",
                "Gender    :",
                "Caste    :",
                "Height    :",
                "Blood Group    :",
                "Date Of Birth    :",
                "Mobile No-    :",
                "Marital Status    :",
                "Email Id    :",
                "Religion    :",
                "Nationality    :",
                "Address    :",
                "Languages Knows    :",
                "Education    :",
                "Computer Knowledge    :",
                "Work    :",
                "Work Experience    :"
            )

            val personalStartX = borderMargin.toFloat() + 10
            val personalStartY = headingStartY + 40

            drawTextList(canvas, marriagePersonalInfoArrayList, data.subList(0, marriagePersonalInfoArrayList.size), personalStartX, personalStartY, textPaint)

            val familyHeading = "Declaration-"
            val familyHeadingPaint = Paint().apply {
                color = Color.RED
                isFakeBoldText = true
                textSize = 22.0f
            }

            val familyHeadingStartY = personalStartY + marriagePersonalInfoArrayList.size * 26 + 20
            canvas.drawText(familyHeading, personalStartX, familyHeadingStartY.toFloat(), familyHeadingPaint)

            val marriageFamilyInfoArrayList = arrayListOf(
                "Declaration     :",
                "Place    :",
                "Date    :"
            )

            val familyStartY = familyHeadingStartY + 26
            drawTextList(canvas, marriageFamilyInfoArrayList, data.subList(marriagePersonalInfoArrayList.size, marriagePersonalInfoArrayList.size + marriageFamilyInfoArrayList.size), personalStartX, familyStartY, textPaint)

            val signatureText = "Signature"
            val signaturePaint = Paint().apply {
                color = Color.BLACK
                isFakeBoldText = true
                textSize = 20.0f
            }

            // Position the signature text slightly higher
            val signatureStartX = canvas.width - borderMargin - 100f // Adjust as needed
            val signatureStartY = canvas.height - borderMargin.toFloat() - 60 // Moved up from -30
            canvas.drawText(signatureText, signatureStartX, signatureStartY, signaturePaint)

            myPdfDocument.finishPage(page)

            val outputStream = ByteArrayOutputStream()
            myPdfDocument.writeTo(outputStream)
            myPdfDocument.close()

            return outputStream.toByteArray()
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

}