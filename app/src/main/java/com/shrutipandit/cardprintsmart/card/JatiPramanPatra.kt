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
            BitmapFactory.decodeResource(context.resources, R.drawable.aaypartapagepamplet)
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


        // Example dynamic data from schoolData list
        val formNumber = schoolData.getOrNull(0) ?: "To Missing"
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
        val postoffice = schoolData.getOrNull(11) ?: "Sir/Mam Missing"
        val prakhanad = schoolData.getOrNull(12) ?: "Body Missing"
        val anumnadal = schoolData.getOrNull(13) ?: "Applicant Name Missing"
        val caste = schoolData.getOrNull(14) ?: "Date Missing"
        val anusuchi = schoolData.getOrNull(15) ?: "Subject Missing"
        val anukramank = schoolData.getOrNull(16) ?: "Sir/Mam Missing"
        val digitallySignedBy = schoolData.getOrNull(17) ?: "Body Missing"
        val dateAndTime = schoolData.getOrNull(18) ?: "Applicant Name Missing"


        // Centered Header
        canvas.drawText("बिहार सरकार", pageWidth / 2, 50f, headerPaint)
        canvas.drawText("Government of Bihar", pageWidth / 2, 70f, headerPaint)
        canvas.drawText("फॉर्म / Form-$formNumber", startXform, startYform, boldTextPaint)

        val lines = listOf(

            "             जिला / District: $district,अनुमंडल/Sub-Division:$district,अंचल/Circle:$circle$pageWidth",
            "                               अत्यंत पिछड़ा वर्ग का जाति प्रमाण-पत्र / Caste Certificate of EBC",
            "                                               (बिहार सरकार के प्रयोजनार्थ)",
            "",
            "प्रमाण-पत्र संख्या: $pramanPatranumber                                                    दिनांक: $date",
            "",
            "        प्रमाणित किया जाता है कि $name($name), पिता (Father) $fatherName($fatherName), माता (Mother)",
            "$motherName($motherName),ग्राम / मोहला - $village,थाना - $policeStation,प्रखंड - $prakhanad,अनुमंडल -",
            "$district,जिला - $district,राज्य - बिहार के प्रजाति($caste)समुदाय के सदस्य है, जो बिहार पदों एवं सेवाओं की रिक्तियों में आरक्छन",
            "(अनुसूचित जातियों, अनुसूचित जनजातियों एवं अन्य पिछडे वर्षों के लिए) अधिनियम, 1991 समय-समय पर यथासंशोधित अधिनियम",
            " के अंतर्गत बिहार राज्य की अत्यंत पिछड़ा वर्ग (अनुसूची-1) में अनुक्रमांक 97 पर अंकित है। अतः $name($name), पिता",
            "(Father)$fatherName($fatherName), अत्यंत पिछडा वर्ग (अनुसूची-1) के हैं।",
            "",
            "        $name ($name) एवं उनका परिवार वर्तमान में ग्राम / मोहला -$village, डाकघर - $postoffice, थाना - $policeStation,",
            " प्रखंड - $prakhanad, अनुमंडल - $anumandal, जिला- $district, राज्य - BIHAR में निवास करता हैं।",

            "                                                                 Digitally signed by $digitallySignedBy",
            "                                                                 Date:$dateAndTime",

            "स्थान: $prakhanad                                          (हस्ताक्षर राजस्व अधिकारी / Signature",
            "दिनांक: $date                                                            Revenue Officer)",

            "",

            "OR Code की जाँच https://servicsonline.bihar.gov.in पोर्टल एवं Play Store पर उपलब्ध ServicePlus",
            "Mobile App से करें।",
            "वैधताः कोई समय सीमा नहीं।",
            "",
            "",
            "",

            "Reference No: $pramanPatranumber To View: https://serviceonline.bihar.gov.in/officials/it/NKDrf/22141203 Tokan No: 22141203"

        )

        for (line in lines) {
            canvas.drawText(line, startX, startY, textPaint)
            startY += lineSpacing
        }
//        for (lines in lines) {
//            canvas.drawText(lines, startXs, startYs, textPaint)
//            startY += lineSpacing
//        }

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