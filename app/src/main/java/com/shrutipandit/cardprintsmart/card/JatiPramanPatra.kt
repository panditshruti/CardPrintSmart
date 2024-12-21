package com.shrutipandit.cardprintsmart.card

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.lifecycle.ViewModel
import com.itextpdf.barcodes.qrcode.EncodeHintType
import com.shrutipandit.cardprintsmart.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class JatiPramanPatra : ViewModel() {

    private var data: MutableList<String> = mutableListOf()

    fun setData(data: List<String>) {
        this.data = data.toMutableList()
    }

    fun generateJatiPramanPatra(
        context: Context,
        schoolData: List<String>,
        inputLanguage: String = "en" // Default input language
    ): ByteArray {
        val myPdfDocument = PdfDocument()

        // Page size: A4 (portrait)
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        var page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)
        var canvas = page.canvas

        // Load and draw the background image
        val bgBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.aaypartapagepamplet) // Replace 'maragepampletcard' with your actual drawable name
        val scaledBgBitmap = Bitmap.createScaledBitmap(bgBitmap, canvas.width, canvas.height, true)
        canvas.drawBitmap(scaledBgBitmap, 0f, 0f, null)


        // Text Paint
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 13.0f
        }

        // Extract Data
        val formName = schoolData.getOrNull(0) ?: "To Missing"
        val district = schoolData.getOrNull(1) ?: "School Address Missing"
        val anumandal = schoolData.getOrNull(2) ?: "Subject Missing"
        val circle = schoolData.getOrNull(3) ?: "Sir/Mam Missing"
        val pramanPatranumber = schoolData.getOrNull(4) ?: "Body Missing"
        val date = schoolData.getOrNull(5) ?: "Applicant Name Missing"
        val name = schoolData.getOrNull(6) ?: "Date Missing"
        val fatherName = schoolData.getOrNull(7) ?: "To Missing"
        val motherName = schoolData.getOrNull(8) ?: "School Address Missing"
        val village = schoolData.getOrNull(9) ?: "Subject Missing"
        val policeStation = schoolData.getOrNull(10) ?: "Sir/Mam Missing"
        val prakhanad = schoolData.getOrNull(11) ?: "Body Missing"
        val anumnadal = schoolData.getOrNull(12) ?: "Applicant Name Missing"
        val caste = schoolData.getOrNull(13) ?: "Date Missing"
       val anusuchi = schoolData.getOrNull(14) ?: "Subject Missing"
        val anukramank = schoolData.getOrNull(15) ?: "Sir/Mam Missing"
        val digitallySignedBy = schoolData.getOrNull(16) ?: "Body Missing"
        val dateAndTime = schoolData.getOrNull(17) ?: "Applicant Name Missing"

        val englishText = """
        To,
    """.trimIndent()

        // Constants for positioning
        val startX = 50f
        var startY = 100f
        val lineSpacing = 20f
        val pageHeight = 842 - 100 // Leave bottom margin
        val pageWidth = 595 - 100 // Leave side margins

        // Split English text into lines
        val lines = englishText.split("\n")

        // Process each line and ensure correct formatting
        for (line in lines) {
            // Check if the line fits in the remaining width or wrap it
            val words = line.split(" ")
            var currentLine = ""

            for (word in words) {
                val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
                val textWidth = textPaint.measureText(testLine)

                if (textWidth > pageWidth) { // Line exceeds page width, wrap it
                    if (startY + lineSpacing > pageHeight) {
                        // Finish the current page and create a new one
                        myPdfDocument.finishPage(page)
                        page = myPdfDocument.startPage(pageInfo)
                        canvas = page.canvas
                        startY = 100f // Reset start position for new page
                    }
                    // Draw the current line
                    canvas.drawText(currentLine, startX, startY, textPaint)
                    currentLine = word // Start a new line with the current word
                    startY += lineSpacing
                } else {
                    currentLine = testLine
                }
            }

            // Draw any remaining text in the current line
            if (currentLine.isNotEmpty()) {
                if (startY + lineSpacing > pageHeight) {
                    // Finish the current page and create a new one
                    myPdfDocument.finishPage(page)
                    page = myPdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    startY = 100f // Reset start position for new page
                }
                canvas.drawText(currentLine, startX, startY, textPaint)
                startY += lineSpacing
            }

            // Add additional spacing for empty lines (if any)
            if (line.isBlank()) {
                startY += lineSpacing
            }
        }

        myPdfDocument.finishPage(page)

        // Write PDF to output
        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }


//    private fun generateQrCode(content: String): Bitmap {
//        val size = 200 // Width and height of the QR code
//        val hints = mapOf(EncodeHintType.MARGIN to 1) // Reduce white margin
//        val bitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
//        val width = bitMatrix.width
//        val height = bitMatrix.height
//        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
//
//        for (x in 0 until width) {
//            for (y in 0 until height) {
//                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
//            }
//        }
//        return bitmap
//    }

    /**
     * Generate and save the caste certificate as a PDF file.
     */
    fun generateCasteCertificatePdf(context: Context, schoolData: List<String>): ByteArray? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // Background setup
        val bgBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.aaypartapagepamplet)
        val scaledBgBitmap = Bitmap.createScaledBitmap(bgBitmap, canvas.width, canvas.height, true)
        canvas.drawBitmap(scaledBgBitmap, 0f, 0f, null)

        // Paint setup
        val headerPaint = Paint().apply {
            color = Color.BLACK
            textSize = 14f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }

        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 12f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        }

        val boldTextPaint = Paint().apply {
            color = Color.BLACK
            textSize = 12f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }

        // Constants for positioning
        val pageWidth = pageInfo.pageWidth.toFloat()
        val startX = 50f
        var startY = 200f
        val lineSpacing = 20f

        val form = pageInfo.pageWidth.toFloat()
        val startXform = 260f
        var startYform = 90f
        val lineSpacingss = 20f

        // Centered Header
        canvas.drawText("बिहार सरकार", pageWidth / 2, 50f, headerPaint)
        canvas.drawText("Government of Bihar", pageWidth / 2, 70f, headerPaint)
        canvas.drawText("फॉर्म / Form-IV", startXform, startYform, boldTextPaint)

        startY += 1 * lineSpacing

        // Example dynamic data from schoolData list
        val district = schoolData.getOrNull(1) ?: "District Missing"
        val pramanPatraNumber = schoolData.getOrNull(4) ?: "Certificate No Missing"
        val date = schoolData.getOrNull(5) ?: "Date Missing"
        val name = schoolData.getOrNull(6) ?: "Name Missing"
        val fatherName = schoolData.getOrNull(7) ?: "Father Missing"
        val motherName = schoolData.getOrNull(8) ?: "Mother Missing"

        // Body content with dynamic placeholders
        val lines = listOf(
            "जिला / District: $district",
            "अत्यंत पिछड़ा वर्ग का जाति प्रमाण-पत्र / Caste Certificate of EBC",
            "(बिहार सरकार के प्रयोजनार्थ)",
            "",
            "प्रमाण-पत्र संख्या: $pramanPatraNumber                  दिनांक: $date",
            "",
            "        प्रमाणित किया जाता है कि $name, पिता (Father) $fatherName, माता (Mother) $motherName,",
            "जाति - कानु / साहू, उपजाति - साहु, बथनाहा, सीतामढ़ी, राज्य - बिहार में निवास करता है।",
            "",
            "        यह प्रमाण-पत्र उक्त $name के माता-पिता द्वारा प्रदान किए गए साक्ष्य और",
            "वर्तमान सरकारी प्रलेखों के आधार पर सत्यापित किया गया है। यह प्रमाण-पत्र राज्य सरकार के",
            "दस्तावेजों में मान्य होगा।",
            "",
            "स्थान: बथनाहा",
            "दिनांक: $date",
            "",
            "डिजिटल हस्ताक्षर: गीता लाल प्रसाद",
            "Revenue Officer"
        )

        for (line in lines) {
            canvas.drawText(line, startX, startY, textPaint)
            startY += lineSpacing
        }

        // Finish the page
        pdfDocument.finishPage(page)

        // Write PDF to ByteArrayOutputStream
        val outputStream = ByteArrayOutputStream()
        return try {
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            outputStream.toByteArray() // Return the byte array for further use
        } catch (e: IOException) {
            e.printStackTrace()
            println("Error saving PDF: ${e.message}")
            null
        }
    }


}
