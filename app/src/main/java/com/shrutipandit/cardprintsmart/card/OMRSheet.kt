import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.ByteArrayOutputStream

class OMRSheet {

    fun generatePdf(context: Context, numberOfQuestions: Int, paperSize: String, schoolName: String): ByteArray {
        val fixedHeaderHeight = 150
        val rowHeight = 50
        val padding = 20
        val maxColumns = 4
        val pageWidth = 595
        val pageHeight = 842

        val questionsPerColumn = ((pageHeight - fixedHeaderHeight - padding) / rowHeight).toInt()
        val questionsPerPage = maxColumns * questionsPerColumn
        val totalPages = Math.ceil(numberOfQuestions / questionsPerPage.toDouble()).toInt()

        val myPdfDocument = PdfDocument()

        for (pageIndex in 0 until totalPages) {
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageIndex + 1).create()
            val page = myPdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            canvas.drawColor(Color.WHITE)

            if (pageIndex == 0) drawHeader(canvas, padding, schoolName)

            val startQuestionIndex = pageIndex * questionsPerPage + 1
            val endQuestionIndex = minOf(startQuestionIndex + questionsPerPage - 1, numberOfQuestions)
            drawQuestions(canvas, startQuestionIndex, endQuestionIndex, padding, fixedHeaderHeight, rowHeight, maxColumns)

            myPdfDocument.finishPage(page)
        }

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }
    private fun drawHeader(canvas: Canvas, padding: Int, schoolName: String): Float {
        // Paint for the school name
        val schoolNamePaint = Paint().apply {
            color = Color.BLACK
            textSize = 20f
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD) // Bold text
            isAntiAlias = true
            textAlign = Paint.Align.CENTER // Center-align the text
        }

        val headerPaint = Paint().apply {
            color = Color.BLACK
            textSize = 14f
            isAntiAlias = true
        }

        // Draw the school name centered at the top
        val centerX = (canvas.width / 2).toFloat() // Center X position
        var currentY = (padding + 40).toFloat() // Initial Y position for the school name

        // Break school name into multiple lines if too long
        val schoolNameLines = wrapText(schoolName, schoolNamePaint, canvas.width - 2 * padding)
        for (line in schoolNameLines) {
            canvas.drawText(line, centerX, currentY, schoolNamePaint)
            currentY += 30 // Line spacing
        }

        // Adjust Y position after school name
        currentY += 10

        // Draw Name, Roll Number, etc.
        canvas.drawText("Name: ____________________", padding.toFloat(), currentY, headerPaint)
        canvas.drawText("Roll No: ____________________", (canvas.width / 2 + padding).toFloat(), currentY, headerPaint)
        currentY += 30
        canvas.drawText("Class: ____________________", padding.toFloat(), currentY, headerPaint)
        canvas.drawText("Mobile: ____________________", (canvas.width / 2 + padding).toFloat(), currentY, headerPaint)
        currentY += 30
        canvas.drawText("Date: ____________________", padding.toFloat(), currentY, headerPaint)
        canvas.drawText("Sub: ____________________", (canvas.width / 2 + padding).toFloat(), currentY, headerPaint)

        // Separator line
        currentY += 30
        canvas.drawLine(
            padding.toFloat(),
            currentY,
            (canvas.width - padding).toFloat(),
            currentY,
            headerPaint
        )

        // Return the Y position of the separator line (below header)
        return currentY + 10 // Return the position just below the separator line
    }

    private fun drawQuestions(
        canvas: Canvas,
        startQuestion: Int,
        endQuestion: Int,
        padding: Int,
        headerHeight: Int,
        rowHeight: Int,
        maxColumns: Int
    ) {
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 12f
            isAntiAlias = true
        }
        val bubblePaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        val columnWidth = (canvas.width - 2 * padding) / maxColumns
        var currentQuestion = startQuestion

        // Get the Y position for the questions start
        val separatorLineY = headerHeight + padding + 30 // Separator line Y position
        val constantGap = 20 // Fixed gap between the separator line and the first question
        val questionsStartY = separatorLineY + constantGap

        // Adjust questions Y-position for multiple header lines
        var currentY = questionsStartY

        for (col in 0 until maxColumns) {
            var yPos = currentY
            val xPos = padding + col * columnWidth

            while (yPos + rowHeight < canvas.height && currentQuestion <= endQuestion) {
                // Draw question number
                canvas.drawText("$currentQuestion.", xPos.toFloat(), yPos.toFloat(), textPaint)

                // Draw bubbles
                val bubbleStartX = xPos + 30
                drawBubbles(canvas, bubbleStartX.toFloat(), yPos.toFloat(), bubblePaint)

                currentQuestion++
                yPos += rowHeight
            }
        }
    }


    private fun drawBubbles(canvas: Canvas, startX: Float, yPos: Float, bubblePaint: Paint) {
        val options = listOf("A", "B", "C", "D")
        val bubbleRadius = 10f // Bubble size
        val optionGap = 25 // Gap between options

        for ((index, option) in options.withIndex()) {
            val bubbleX = startX + index * optionGap
            canvas.drawCircle(bubbleX, yPos - 5, bubbleRadius, bubblePaint) // Bubble
            canvas.drawText(option, bubbleX - 5, yPos + 15, bubblePaint) // Option text
        }
    }

    private fun wrapText(text: String, paint: Paint, maxWidth: Int): List<String> {
        val words = text.split(" ")
        val lines = mutableListOf<String>()
        var currentLine = StringBuilder()

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else currentLine.toString() + " " + word
            if (paint.measureText(testLine) <= maxWidth) {
                currentLine.append(" ").append(word)
            } else {
                lines.add(currentLine.toString())
                currentLine = StringBuilder(word)
            }
        }

        // Add the last line
        if (currentLine.isNotEmpty()) {
            lines.add(currentLine.toString())
        }

        return lines
    }



}
