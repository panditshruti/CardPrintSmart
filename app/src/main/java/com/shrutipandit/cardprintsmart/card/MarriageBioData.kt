package com.shrutipandit.cardprintsmart.card

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import androidx.lifecycle.ViewModel
import com.shrutipandit.cardprintsmart.R
import java.io.ByteArrayOutputStream
import java.io.IOException

class MarriageBioData : ViewModel() {

    private var data: MutableList<String> = mutableListOf()

    fun setData(data: MutableList<String>) {
        this.data = data
    }

    fun generatePdf(context: Context): ByteArray {
        val myPdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        val image = BitmapFactory.decodeResource(context.resources, R.drawable.shadicard)
        val imageRect = Rect(0, 0, canvas.width, canvas.height)
        canvas.drawBitmap(image, null, imageRect, null)

        val textPaint = Paint()
        textPaint.color = Color.BLACK
        textPaint.textSize = 20.5f

        // Draw Heading "MarriageBioData"
        val margin = 20
        val headingText = "MarriageBioData"
        val headingPaint = Paint().apply {
            color = Color.RED
            isFakeBoldText = true
            textSize = 30.0f
        }
        val textWidth = headingPaint.measureText(headingText)
        val startX = (canvas.width - textWidth) / 2
        val startY = 50F + margin
        canvas.drawText(headingText, startX, startY, headingPaint)

        // Draw Heading "Personal Information"
        val personalHeading = "Personal Information"
        val personalHeadingPaint = Paint().apply {
            color = Color.RED
            isFakeBoldText = true
            textSize = 23.0f
        }
        val personalStartX = 80F
        val personalStartY = startY + 30 + margin
        canvas.drawText(personalHeading, personalStartX, personalStartY, personalHeadingPaint)

        // Define personal and family information lists
        val marriagePersonalInfoArrayList = arrayListOf(
            "Name             :",
            "Father's Name    :",
            "Date Of Birth    :",
            "Birth Place      :",
            "Religion         :",
            "Caste            :",
            "Height           :",
            "Blood Group      :",
            "Complexion       :",
            "Education        :",
            "10 Marks         :",
            "12 Marks         :",
            "Post             :",
            "District         :",
            "State            :"
        )
        val marriageFamilyInfoArrayList = arrayListOf(
            "Father Name      :",
            "Occupation       :",
            "Mother Name      :",
            "Occupation       :",
            "Sister           :",
            "Brother          :"
        )

        val lineHeight = 25

        // Draw Personal Information
        drawTextList(
            canvas,
            marriagePersonalInfoArrayList,
            data.take(marriagePersonalInfoArrayList.size), // Safely take only available data
            personalStartX,
            personalStartY + lineHeight,
            textPaint
        )

        // Draw Heading "Family Information"
        val familyHeading = "Family Information"
        val familyHeadingPaint = Paint().apply {
            color = Color.RED
            isFakeBoldText = true
            textSize = 23.0f
        }
        val familyStartY = personalStartY + lineHeight * (marriagePersonalInfoArrayList.size + 1) + margin * 0.5F
        canvas.drawText(familyHeading, personalStartX, familyStartY, familyHeadingPaint)

        // Draw Family Information
        if (data.size >= marriagePersonalInfoArrayList.size + marriageFamilyInfoArrayList.size) {
            drawTextList(
                canvas,
                marriageFamilyInfoArrayList,
                data.subList(marriagePersonalInfoArrayList.size, marriagePersonalInfoArrayList.size + marriageFamilyInfoArrayList.size),
                personalStartX,
                familyStartY + lineHeight,
                textPaint
            )
        } else {
            // Handle case where family information data is insufficient (e.g., skip or fill with placeholders)
            drawTextList(
                canvas,
                marriageFamilyInfoArrayList,
                List(marriageFamilyInfoArrayList.size) { "" }, // Placeholder empty data
                personalStartX,
                familyStartY + lineHeight,
                textPaint
            )
        }

        // Draw Heading "Contact Information"
        val contactHeading = "Contact Information"
        val contactHeadingPaint = Paint().apply {
            color = Color.RED
            isFakeBoldText = true
            textSize = 23.0f
        }
        val contactStartY = familyStartY + lineHeight * (marriageFamilyInfoArrayList.size + 1) + margin
        canvas.drawText(contactHeading, personalStartX, contactStartY, contactHeadingPaint)

        // Draw Contact Information
        val marriageContactInfoArrayList = arrayListOf(
            "Address          :",
            "Contact No-      :"
        )
        if (data.size >= marriagePersonalInfoArrayList.size + marriageFamilyInfoArrayList.size + marriageContactInfoArrayList.size) {
            drawTextList(
                canvas,
                marriageContactInfoArrayList,
                data.subList(marriagePersonalInfoArrayList.size + marriageFamilyInfoArrayList.size, data.size),
                personalStartX,
                contactStartY + lineHeight,
                textPaint
            )
        } else {
            // Handle case where contact information data is insufficient (e.g., skip or fill with placeholders)
            drawTextList(
                canvas,
                marriageContactInfoArrayList,
                List(marriageContactInfoArrayList.size) { "" }, // Placeholder empty data
                personalStartX,
                contactStartY + lineHeight,
                textPaint
            )
        }

        myPdfDocument.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }


    private fun drawTextList(canvas: android.graphics.Canvas, labelList: List<String>, dataList: List<String>, x: Float, y: Float, paint: Paint) {
        var yPoint = y
        for (i in labelList.indices) {
            val text = "${labelList[i]} ${dataList.getOrNull(i) ?: ""}"
            canvas.drawText(text, x, yPoint, paint)
            yPoint += 25 // Adjust line height
        }
    }

}