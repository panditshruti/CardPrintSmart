package com.shrutipandit.cardprintsmart.card

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.shrutipandit.cardprintsmart.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MarriageBioData : ViewModel() {

    private var data: MutableList<String> = mutableListOf()

    fun templet1(context: Context) {
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

        // Draw Heading "MarriageBioData" with margin
        val margin = 20 // Margin value
        val headingText = "MarriageBioData"
        val headingPaint = Paint().apply {
            color = Color.RED
            isFakeBoldText = true
            textSize = 30.0f // Adjust size as needed
        }
        val textWidth = headingPaint.measureText(headingText)
        val startX = (canvas.width - textWidth) / 2 // Center horizontally
        val startY = 50F + margin // Adjust Y position with margin
        canvas.drawText(headingText, startX, startY, headingPaint)

        // Draw Heading "Personal Information" with gap
        val personalHeading = "Personal Information"
        val personalHeadingPaint = Paint().apply {
            color = Color.RED
            isFakeBoldText = true
            textSize = 23.0f // Adjust size as needed
        }
        val personalStartX = 80F // Align to left
        val personalStartY = startY + 30 + margin // Adjust Y position with margin
        canvas.drawText(personalHeading, personalStartX, personalStartY, personalHeadingPaint)

        // Draw Personal Information
        val marriagePersonalInfoArrayList = arrayListOf(
            "Name             :",
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
        val lineHeight = 26
        drawTextList(canvas, marriagePersonalInfoArrayList, personalStartX, personalStartY + lineHeight, textPaint)

        // Draw Heading "Family Information" with reduced gap
        val familyHeading = "Family Information"
        val familyHeadingPaint = Paint().apply {
            color = Color.RED
            isFakeBoldText = true
            textSize = 23.0f // Adjust size as needed
        }
        val familyStartY = personalStartY + lineHeight * (marriagePersonalInfoArrayList.size + 1) + margin * 0.5F // Adjust Y position with reduced margin
        canvas.drawText(familyHeading, personalStartX, familyStartY, familyHeadingPaint)

        // Draw Family Information
        val marriageFamilyInfoArrayList = arrayListOf(
            "Father Name      :",
            "Occupation       :",
            "Mother Name      :",
            "Occupation       :",
            "Sister           :",
            "Brother          :"
        )
        drawTextList(canvas, marriageFamilyInfoArrayList, personalStartX, familyStartY + lineHeight, textPaint)

        // Draw Heading "Contact Information" with gap
        val contactHeading = "Contact Information"
        val contactHeadingPaint = Paint().apply {
            color = Color.RED
            isFakeBoldText = true
            textSize = 23.0f // Adjust size as needed
        }
        val contactStartY = familyStartY + lineHeight * (marriageFamilyInfoArrayList.size + 1) + margin // Adjust Y position with margin
        canvas.drawText(contactHeading, personalStartX, contactStartY, contactHeadingPaint)

        // Draw Contact Information
        val marriageContactInfoArrayList = arrayListOf(
            "Address          :",
            "Contact No-      :"
        )
        drawTextList(canvas, marriageContactInfoArrayList, personalStartX, contactStartY + lineHeight, textPaint)

        myPdfDocument.finishPage(page)

        val file2: File = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "e_marriage_card.pdf"
        )
        try {
            myPdfDocument.writeTo(FileOutputStream(file2))
            Toast.makeText(context, "Ready to Print", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun drawTextList(canvas: android.graphics.Canvas, textList: List<String>, x: Float, y: Float, paint: Paint) {
        var yPoint = y
        for (text in textList) {
            canvas.drawText(text, x, yPoint, paint)
            yPoint += 25 // Adjust line height
        }
    }
}
