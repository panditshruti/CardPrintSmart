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

    fun generateCasteCertificatePdf(context: Context, schoolData: List<String>): ByteArray? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // Background setup
        val bgBitmap =
            BitmapFactory.decodeResource(context.resources, R.drawable.jatip4)
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
        val startX = 20f
        var startY = 220f
        val lineSpacing = 20f
     val pageWidtha = pageInfo.pageWidth.toFloat()
        val startXa= 40f
        val form = pageInfo.pageWidth.toFloat()
        val startXform = 260f
        var startYform = 150f



        // Example dynamic data from schoolData list
        val formNumber = schoolData.getOrNull(0) ?: "To Missing"
        val district = schoolData.getOrNull(1) ?: "School Address Missing"
        val anumandal = schoolData.getOrNull(2) ?: "Subject Missing"
        val circle = schoolData.getOrNull(3) ?: "Sir/Mam Missing"
        val pramanPatraNumber = schoolData.getOrNull(4) ?: "Body Missing"
        val date = schoolData.getOrNull(5) ?: "Applicant Name Missing"
        val name = schoolData.getOrNull(6) ?: "Date Missing"
        val hindiName = schoolData.getOrNull(7) ?: "Date Missing"
        val fatherName = schoolData.getOrNull(8) ?: "To Missing"
        val hindiFatherName = schoolData.getOrNull(9) ?: "To Missing"
        val motherName = schoolData.getOrNull(10) ?: "School Address Missing"
        val hindiMotherName = schoolData.getOrNull(11) ?: "School Address Missing"
        val village = schoolData.getOrNull(12) ?: "Subject Missing"
        val policeStation = schoolData.getOrNull(13) ?: "Sir/Mam Missing"
        val postoffice = schoolData.getOrNull(14) ?: "Sir/Mam Missing"
        val prakhanad = schoolData.getOrNull(15) ?: "Body Missing"
        val caste = schoolData.getOrNull(16) ?: "Date Missing"
        val anusuchi = schoolData.getOrNull(17) ?: "Subject Missing"
        val anukramank = schoolData.getOrNull(18) ?: "Sir/Mam Missing"
        val digitallySignedBy = schoolData.getOrNull(19) ?: "Body Missing"
        val dateAndTime = schoolData.getOrNull(20) ?: "Applicant Name Missing"


        val qrCodeContent = "Reference No: $pramanPatraNumber To View: https://serviceonline.bihar.gov.in"
        val qrCodeBitmap = generateQRCode(qrCodeContent, 20,0,130) // QR Code size: 150x150
//        canvas.drawBitmap(qrCodeBitmap, 0f, 0f, null)

        // Centered Header
        canvas.drawText("बिहार सरकार", pageWidth / 2, 100f, headerPaint)
        canvas.drawText("Government of Bihar", pageWidth / 2, 115f, headerPaint)
        canvas.drawText("फॉर्म / Form-$formNumber", startXform, 130f, boldTextPaint)

        canvas.drawText("जिला / District: $district,अनुमंडल/Sub-Division:$district,अंचल/Circle:$circle", pageWidth / 2, 165f, headerPaint)
        canvas.drawText("अत्यंत पिछड़ा वर्ग का जाति प्रमाण-पत्र / Caste Certificate of EBC", pageWidth / 2, 185f, headerPaint)
        canvas.drawText("(बिहार सरकार के प्रयोजनार्थ)", pageWidth / 2, 205f, headerPaint)

        // Left Side: प्रमाण-पत्र संख्या (Fixed Position)
        val pramanPatraLabel = "प्रमाण-पत्र संख्या:"
        val pramanPatraValue = pramanPatraNumber
        val pramanPatraX = 70f
        canvas.drawText(pramanPatraLabel, pramanPatraX, 230f, headerPaint)

// प्रमाण-पत्र संख्या value ko bilkul just baad likhna (extra gap hata diya)
        val pramanPatraLabelWidth = headerPaint.measureText(pramanPatraLabel)
        val pramanPatraValueX = pramanPatraX + pramanPatraLabelWidth + 2f  // Sirf 5px ka gap
        canvas.drawText(pramanPatraValue, pramanPatraValueX, 230f, headerPaint)
        val rightPadding = 0f // Pehle se jo gap tha, usko maintain karega

// Right Side: दिनांक (Fixed at End of Page)
        val dateText = "दिनांक: $date"
        val dateTextWidth = headerPaint.measureText(dateText)

// Ensure date is right-aligned with fixed padding
        val dateX = pageWidth - dateTextWidth - rightPadding
        canvas.drawText(dateText, dateX, 230f, headerPaint)




        val lines = listOf(
//            "                 जिला / District: $district,अनुमंडल/Sub-Division:$district,अंचल/Circle:$circle                          ",
//            "                              अत्यंत पिछड़ा वर्ग का जाति प्रमाण-पत्र / Caste Certificate of EBC                                  ",
//            "                                             (बिहार सरकार के प्रयोजनार्थ)                                                     ",
            "",
//            "प्रमाण-पत्र संख्या: $pramanPatraNumber                                                                            दिनांक: $date",
            "",
            "        प्रमाणित किया जाता है कि $name($hindiName), पिता (Father) $fatherName($hindiFatherName),",
            " माता (Mother)$motherName($hindiMotherName),ग्राम / मोहला - $village,थाना - $policeStation,प्रखंड - $prakhanad,अनुमंडल -$anumandal",
            "जिला - $district,राज्य - बिहार के प्रजाति($caste)समुदाय के सदस्य है, जो बिहार पदों एवं सेवाओं की रिक्तियों में आरक्छन",
            "(अनुसूचित जातियों, अनुसूचित जनजातियों एवं अन्य पिछडे वर्षों के लिए) अधिनियम, 1991 समय-समय पर यथासंशोधित अधिनियम",
            " के अंतर्गत बिहार राज्य की अत्यंत पिछड़ा वर्ग (अनुसूची-$anusuchi) में अनुक्रमांक $anukramank पर अंकित है। अतः $name($hindiName)",
            " पिता (Father)$fatherName($fatherName), अत्यंत पिछडा वर्ग (अनुसूची-$anusuchi) के हैं।",
            "",
            "        $name ($name) एवं उनका परिवार वर्तमान में ग्राम / मोहला -$village, डाकघर - $postoffice, थाना - $policeStation,",
            " प्रखंड - $prakhanad, अनुमंडल - $anumandal, जिला- $district, राज्य - BIHAR में निवास करता हैं।",

             "                                                                              Digitally signed by $digitallySignedBy",
            "                                                                               Date:$dateAndTime",

            "स्थान: $prakhanad                                                                                                 ",
            "दिनांक: $date                                                      (हस्ताक्षर राजस्व अधिकारी / Signature Revenue Officer)",
            "",
            "",
            "",
            "",
            "",
            "$qrCodeContent",
            "OR Code की जाँच https://servicsonline.bihar.gov.in पोर्टल एवं Play Store पर उपलब्ध ServicePlus",
            "Mobile App से करें।",
            "वैधताः कोई समय सीमा नहीं।",
            "",
            "",
            "",
            "",
            "",
            "",
            "",

            "Reference No:$pramanPatraNumber To View: https://serviceonline.bihar.gov.in/officials/it/NKDrf/22141203 ",
                    "Tokan No: 22141203"

        )

        for (line in lines) {
            if (line == "$qrCodeContent") {
                // Draw the QR code image at the designated position
                val qrCodeBitmap = generateQRCode(qrCodeContent, 20,0,100) // QR Code size: 150x150
                val qrCodeX = startX // Same starting X position as the text
                val qrCodeY = startY // Use current Y position for the QR code
                canvas.drawBitmap(qrCodeBitmap, qrCodeX, qrCodeY - 100f, null) // Adjust to place QR code properly

                // Do not increment `startY` for QR code since it's fixed
            } else {
                // Draw text for all other lines
                canvas.drawText(line, startX, startY, textPaint)
                startY += lineSpacing // Increment Y-coordinate for the next line
            }
        }
        // Add QR Code
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

    fun generateQRCode(content: String, x:Int,y:Int,size: Int): Bitmap {
        val bitMatrix = com.google.zxing.MultiFormatWriter().encode(
            content,
            com.google.zxing.BarcodeFormat.QR_CODE,
            size,
            size,
            mapOf(com.google.zxing.EncodeHintType.MARGIN to 1)
        )
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }

}