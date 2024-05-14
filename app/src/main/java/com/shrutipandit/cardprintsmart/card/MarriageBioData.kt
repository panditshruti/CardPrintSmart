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

class MarriageBioData :ViewModel(){

    private var data: MutableList<String> = mutableListOf()

    fun templet1(context: Context) {
        val myPdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val image =
            BitmapFactory.decodeResource(context.resources, R.drawable.marriagecard1)
        val imageRect = Rect(0, 0, canvas.width, canvas.height)
        canvas.drawBitmap(image, null, imageRect, null)

        val textPaint = Paint()
        textPaint.color = Color.rgb(0, 0, 0)
        textPaint.textSize = 20.5f


        val marriageArrayList = arrayListOf(
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
            "State            :",
            "Fimaly Information",
            "Father Name      :",
            "Occupation       :",
            "Mother Name      :",
            "Occupation       :",
            "Sister           :",
            "Brother          :",
            "Contact Information",
            "Address          :",
            "Contact No-      :",

        )

        val x = 70F
        var yPoint = 150F


        for (i in marriageArrayList) {
            canvas.drawText(i, x, yPoint, textPaint)
            yPoint += 30
        }

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
    }}
