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

class ResumeCard:ViewModel() {


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

        // Draw the heading at the top
        val headingPaint = Paint().apply {
            color = Color.RED
            isFakeBoldText = true
            textSize = 30.0f
        }
        val headingText = "Resume"
        val textWidth = headingPaint.measureText(headingText)
        val headingStartX = (canvas.width - textWidth) / 2
        val headingStartY = borderMargin + 30
        canvas.drawText(headingText, headingStartX, headingStartY.toFloat(), headingPaint)

        // Position for personal information
        val personalStartX = borderMargin.toFloat() + 10
        val personalStartY = headingStartY + 40 // Starts below the heading

        val marriagePersonalInfoArrayList = arrayListOf(
            "Name-    ",
            "Father's Name-    ",
            "Gender-    ",
            "Caste-    ",
            "Date Of Birth-    ",
            "Mobile No-    ",
            "Marital Status-    ",
            "Email Id-    ",
            "Religion-    ",
            "Nationality-    ",
            "Address-    ",
            "Languages Knows-    ",
            "Education-    ",
            "Computer Knowledge-    ",
            "Work-    ",
            "Work Experience-    "
        )

        // Ensure that the data list is large enough to cover all elements
        val personalDataList = data.take(marriagePersonalInfoArrayList.size).toMutableList()
        while (personalDataList.size < marriagePersonalInfoArrayList.size) {
            personalDataList.add("") // Pad with empty strings if necessary
        }

        // Draw the personal information
        drawTextList(canvas, marriagePersonalInfoArrayList, personalDataList, personalStartX, personalStartY, Paint().apply {
            color = Color.BLACK
            textSize = 20.5f
        })

        // Draw the image parallel to the personal information with 10 pixel margin
        val imageMargin = 10
        val imagePaint = Paint()
        val passportWidth = 132 - 2 * imageMargin
        val passportHeight = 170 - 2 * imageMargin
        val imageLeft = canvas.width - borderMargin - passportWidth - 2 * imageMargin // Align the image to the right with margin
        val imageTop = personalStartY + imageMargin // Align with the personal info and margin
        val imageRect = Rect(imageLeft, imageTop, imageLeft + passportWidth, imageTop + passportHeight)
        canvas.drawBitmap(bitmap, null, imageRect, imagePaint)

        // Draw family declaration section
        val familyHeading = "Declaration-"
        val familyHeadingPaint = Paint().apply {
            color = Color.RED
            isFakeBoldText = true
            textSize = 22.0f
        }
        val familyHeadingStartY = personalStartY + marriagePersonalInfoArrayList.size * 26 + 20
        canvas.drawText(familyHeading, personalStartX, familyHeadingStartY.toFloat(), familyHeadingPaint)

        val marriageFamilyInfoArrayList = arrayListOf(
            "Declaration-    ",
            "Place-    ",
            "Date-    "
        )

        // Ensure that the family data list is large enough
        val familyDataList = data.drop(marriagePersonalInfoArrayList.size).take(marriageFamilyInfoArrayList.size).toMutableList()
        while (familyDataList.size < marriageFamilyInfoArrayList.size) {
            familyDataList.add("") // Pad with empty strings if necessary
        }

        val familyStartY = familyHeadingStartY + 26
        drawTextList(canvas, marriageFamilyInfoArrayList, familyDataList, personalStartX, familyStartY, Paint().apply {
            color = Color.BLACK
            textSize = 20.5f
        })

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