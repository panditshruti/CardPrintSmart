package com.shrutipandit.cardprintsmart.card

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.core.content.res.ResourcesCompat
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

        // Set the page size to landscape (swap width and height)
        val pageInfo = PdfDocument.PageInfo.Builder(842, 595, 1).create() // 842x595 for A4 landscape
        val page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        // Load and draw the background image
        val bgBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.maragepampletcard) // Replace 'maragepampletcard' with your actual drawable name
        val scaledBgBitmap = Bitmap.createScaledBitmap(bgBitmap, canvas.width, canvas.height, true)
        canvas.drawBitmap(scaledBgBitmap, 0f, 0f, null)

        // Define Paint for Dulha
        val dulhaPaint = Paint().apply {
            color = Color.RED
            textSize = 60.5f // Text size for Dulha
            typeface = ResourcesCompat.getFont(context, R.font.fonttext)
        }

        // Define Paint for Dulhan
        val dulhanPaint = Paint().apply {
            color = Color.RED
            textSize = 60.0f // Text size for Dulhan
            typeface = ResourcesCompat.getFont(context, R.font.fonttext)
        }

        // Define Paint for Date
        val datePaint = Paint().apply {
            color = Color.YELLOW
            textSize = 30.0f // Text size for Date
            typeface = ResourcesCompat.getFont(context, R.font.fonttext)
        }

        // Static positions for each item
        val dulhaPositionX = 170f
        val dulhaPositionY = 230f

        val dulhanPositionX = 500f
        val dulhanPositionY = 350f

        val datePositionX = 570f
        val datePositionY = 147f

        // Draw Dulha
        val dulhaText = data.getOrNull(0)
        if (dulhaText != null) {
            canvas.drawText(dulhaText, dulhaPositionX, dulhaPositionY, dulhaPaint)
        }

        // Draw Dulhan
        val dulhanText = data.getOrNull(1)
        if (dulhanText != null) {
            canvas.drawText(dulhanText, dulhanPositionX, dulhanPositionY, dulhanPaint)
        }

        // Draw Date
        val dateText = data.getOrNull(2)
        if (dateText != null) {
            canvas.drawText(dateText, datePositionX, datePositionY, datePaint)
        }

        myPdfDocument.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }

    fun generatePdf2(context: Context): ByteArray {
        val myPdfDocument = PdfDocument()

        // Set the page size to landscape (swap width and height)
        val pageInfo = PdfDocument.PageInfo.Builder(842, 595, 1).create() // 842x595 for A4 landscape
        val page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        // Load and draw the background image
        val bgBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.marriagep2) // Replace 'maragepampletcard' with your actual drawable name
        val scaledBgBitmap = Bitmap.createScaledBitmap(bgBitmap, canvas.width, canvas.height, true)
        canvas.drawBitmap(scaledBgBitmap, 0f, 0f, null)

        // Define Paint for Dulha
        val dulhaPaint = Paint().apply {
            color = Color.BLACK
            textSize = 60.5f // Text size for Dulha
            typeface = ResourcesCompat.getFont(context, R.font.fonttext)
        }

        // Define Paint for Dulhan
        val dulhanPaint = Paint().apply {
            color = Color.BLACK
            textSize = 60.0f // Text size for Dulhan
            typeface = ResourcesCompat.getFont(context, R.font.fonttext)
        }

        // Static positions for each item
        val dulhaPositionX = 170f
        val dulhaPositionY = 230f

        val dulhanPositionX = 570f
        val dulhanPositionY = 400f


        // Draw Dulha
        val dulhaText = data.getOrNull(0)
        if (dulhaText != null) {
            canvas.drawText(dulhaText, dulhaPositionX, dulhaPositionY, dulhaPaint)
        }

        // Draw Dulhan
        val dulhanText = data.getOrNull(1)
        if (dulhanText != null) {
            canvas.drawText(dulhanText, dulhanPositionX, dulhanPositionY, dulhanPaint)
        }

        myPdfDocument.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }

    fun generatePdf3(context: Context): ByteArray {
        val myPdfDocument = PdfDocument()

        // Set the page size to landscape (swap width and height)
        val pageInfo = PdfDocument.PageInfo.Builder(842, 595, 1).create() // 842x595 for A4 landscape
        val page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        // Load and draw the background image
        val bgBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.mp2) // Replace 'maragepampletcard' with your actual drawable name
        val scaledBgBitmap = Bitmap.createScaledBitmap(bgBitmap, canvas.width, canvas.height, true)
        canvas.drawBitmap(scaledBgBitmap, 0f, 0f, null)

        val dulhaPaint = Paint().apply {
            color = Color.WHITE
            textSize = 60.5f // Text size for Dulha
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) // Set bold style
            typeface = ResourcesCompat.getFont(context, R.font.fonttext)

        }

        // Define Paint for Dulhan
        val dulhanPaint = Paint().apply {
            color = Color.WHITE
            textSize = 60.5f // Text size for Dulha
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) // Set bold style
            typeface = ResourcesCompat.getFont(context, R.font.fonttext)

        }

        // Static positions for each item
        val dulhaPositionX = 210f
        val dulhaPositionY = 230f

        val dulhanPositionX = 505f
        val dulhanPositionY = 400f


        // Draw Dulha
        val dulhaText = data.getOrNull(0)
        if (dulhaText != null) {
            canvas.drawText(dulhaText, dulhaPositionX, dulhaPositionY, dulhaPaint)
        }

        // Draw Dulhan
        val dulhanText = data.getOrNull(1)
        if (dulhanText != null) {
            canvas.drawText(dulhanText, dulhanPositionX, dulhanPositionY, dulhanPaint)
        }

        myPdfDocument.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }

    fun generatePdf4(context: Context): ByteArray {
        val myPdfDocument = PdfDocument()

        // Set the page size to landscape (swap width and height)
        val pageInfo = PdfDocument.PageInfo.Builder(842, 595, 1).create() // 842x595 for A4 landscape
        val page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        // Load and draw the background image
        val bgBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.mp3) // Replace 'maragepampletcard' with your actual drawable name
        val scaledBgBitmap = Bitmap.createScaledBitmap(bgBitmap, canvas.width, canvas.height, true)
        canvas.drawBitmap(scaledBgBitmap, 0f, 0f, null)

        val dulhaPaint = Paint().apply {
            color = Color.YELLOW
            textSize = 60.5f // Text size for Dulha
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) // Set bold style
            typeface = ResourcesCompat.getFont(context, R.font.fonttext)

        }

        // Define Paint for Dulhan
        val dulhanPaint = Paint().apply {
            color = Color.YELLOW
            textSize = 60.5f // Text size for Dulha
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) // Set bold style
            typeface = ResourcesCompat.getFont(context, R.font.fonttext)

        }

        // Static positions for each item
        val dulhaPositionX = 210f
        val dulhaPositionY = 230f

        val dulhanPositionX = 520f
        val dulhanPositionY = 420f


        // Draw Dulha
        val dulhaText = data.getOrNull(0)
        if (dulhaText != null) {
            canvas.drawText(dulhaText, dulhaPositionX, dulhaPositionY, dulhaPaint)
        }

        // Draw Dulhan
        val dulhanText = data.getOrNull(1)
        if (dulhanText != null) {
            canvas.drawText(dulhanText, dulhanPositionX, dulhanPositionY, dulhanPaint)
        }

        myPdfDocument.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }

    fun generatePdf5(context: Context): ByteArray {
        val myPdfDocument = PdfDocument()

        // Set the page size to landscape (swap width and height)
        val pageInfo = PdfDocument.PageInfo.Builder(842, 595, 1).create() // 842x595 for A4 landscape
        val page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        // Load and draw the background image
        val bgBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.mp6) // Replace 'maragepampletcard' with your actual drawable name
        val scaledBgBitmap = Bitmap.createScaledBitmap(bgBitmap, canvas.width, canvas.height, true)
        canvas.drawBitmap(scaledBgBitmap, 0f, 0f, null)

        val dulhaPaint = Paint().apply {
            color = Color.YELLOW
            textSize = 60.5f // Text size for Dulha
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            typeface = ResourcesCompat.getFont(context, R.font.fonttext)
        // Set bold style

        }

        // Define Paint for Dulhan
        val dulhanPaint = Paint().apply {
            color = Color.YELLOW
            textSize = 60.5f // Text size for Dulha
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) // Set bold style
            typeface = ResourcesCompat.getFont(context, R.font.fonttext)

        }

        // Static positions for each item
        val dulhaPositionX = 210f
        val dulhaPositionY = 230f

        val dulhanPositionX = 520f
        val dulhanPositionY = 420f


        // Draw Dulha
        val dulhaText = data.getOrNull(0)
        if (dulhaText != null) {
            canvas.drawText(dulhaText, dulhaPositionX, dulhaPositionY, dulhaPaint)
        }

        // Draw Dulhan
        val dulhanText = data.getOrNull(1)
        if (dulhanText != null) {
            canvas.drawText(dulhanText, dulhanPositionX, dulhanPositionY, dulhanPaint)
        }

        myPdfDocument.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }



}
