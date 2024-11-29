package com.shrutipandit.cardprintsmart.card

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.lifecycle.ViewModel
import com.shrutipandit.cardprintsmart.R
import java.io.ByteArrayOutputStream

class MarriagePamplet : ViewModel() {

    private var data: MutableList<String> = mutableListOf()

    fun setData(data: List<String>) {
        this.data = data.toMutableList()
    }

    fun generatePdf(context: Context): ByteArray {
        val myPdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        // Load and draw the background image
        val bgBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.maragepampletcard) // Replace 'maragepampletcard' with your actual drawable name
        val scaledBgBitmap = Bitmap.createScaledBitmap(bgBitmap, canvas.width, canvas.height, true)
        canvas.drawBitmap(scaledBgBitmap, 0f, 0f, null)

        // Define Paint for text
        val textPaint = Paint().apply {
            color = Color.RED
            textSize = 50.5f
        }

        // Static positions for each item
        val dulhaPositionX = 100f
        val dulhaPositionY = 200f

        val dulhanPositionX = 100f
        val dulhanPositionY = 300f

        val datePositionX = 100f
        val datePositionY = 400f

        // Draw Dulha
        val dulhaText = "dulha: ${data.getOrNull(0) ?: ""}"
        canvas.drawText(dulhaText, dulhaPositionX, dulhaPositionY, textPaint)

        // Draw Dulhan
        val dulhanText = "dulhan: ${data.getOrNull(1) ?: ""}"
        canvas.drawText(dulhanText, dulhanPositionX, dulhanPositionY, textPaint)

        // Draw Date
        val dateText = "date: ${data.getOrNull(2) ?: ""}"
        canvas.drawText(dateText, datePositionX, datePositionY, textPaint)

        myPdfDocument.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }
}
