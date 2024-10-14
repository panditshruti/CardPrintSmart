package com.shrutipandit.cardprintsmart.card

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.ByteArrayOutputStream

class QuestionPdf {

    fun generateQuestionPdf(context: Context, questions: ArrayList<String>): ByteArrayOutputStream {
        val myPdfDocument = PdfDocument()

        // Define page info: A4 size (595 x 842 points)
        val pageWidth = 595
        val pageHeight = 842
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        // Set up paint for the text
        val paint = Paint().apply {
            color = Color.BLACK // Set text color to black
            textSize = 14f // Adjust text size for visibility
            isFakeBoldText = false
        }

        // Define margins
        val leftMargin = 40
        val topMargin = 50
        val bottomMargin = 50
        val lineSpacing = 20 // Space between lines

        var yPosition = topMargin
        var currentPageNumber = 1

        // Start a new page for each block of questions that fit on one page
        var page = myPdfDocument.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageNumber).create())
        var canvas: Canvas = page.canvas

        // Loop through questions and add them to the PDF
        for ((index, question) in questions.withIndex()) {
            if (yPosition + lineSpacing > pageHeight - bottomMargin) {
                // If the current page is full, finish the page and create a new one
                myPdfDocument.finishPage(page)
                currentPageNumber++
                page = myPdfDocument.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageNumber).create())
                canvas = page.canvas
                yPosition = topMargin // Reset y position for the new page
            }

            // Draw the question text on the current page
            canvas.drawText("${index + 1}. $question", leftMargin.toFloat(), yPosition.toFloat(), paint)
            yPosition += lineSpacing // Move to the next line
        }

        // Finish the last page
        myPdfDocument.finishPage(page)

        // Write the PDF to a byte array
        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        // Return the generated PDF as a byte array
        return outputStream
    }
}
