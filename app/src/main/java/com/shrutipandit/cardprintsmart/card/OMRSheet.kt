import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.ByteArrayOutputStream

class OMRSheet {

    fun generatePdf(context: Context, numberOfQuestions: Int, paperSize: String): ByteArray {
        val fixedHeaderHeight = 80 // Height reserved for header information
        val rowHeight = 40 // Height for each row of questions
        val padding = 20 // Padding around the page

        // Calculate the number of columns based on the number of questions
        val maxColumns = 5 // Maximum number of columns to ensure readability
        val columns = when {
            numberOfQuestions <= 30 -> 3
            numberOfQuestions <= 50 -> 4
            else -> maxColumns
        }

        // Calculate the number of rows needed
        val rows = Math.ceil(numberOfQuestions / columns.toDouble()).toInt()
        val contentHeight = rows * rowHeight
        val pageHeight = fixedHeaderHeight + contentHeight + padding

        // Create page info with the calculated height
        val pageInfo = PdfDocument.PageInfo.Builder(595, pageHeight, 1).create() // Default width for A4, custom height
        val myPdfDocument = PdfDocument()
        val page = myPdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        // Set background color
        canvas.drawColor(Color.WHITE)

        // Paint for fixed text
        val fixedTextPaint = Paint().apply {
            color = Color.BLACK
            textSize = 16f
            isAntiAlias = true
        }
        val startX = padding.toFloat()
        val startY = padding.toFloat()

        // Fixed text
        canvas.drawText("Name: John Doe", startX, startY, fixedTextPaint)
        canvas.drawText("Class: 10th Grade", startX, startY + 20, fixedTextPaint)
        canvas.drawText("Roll Number: 123456", startX, startY + 40, fixedTextPaint)
        canvas.drawText("Mobile Number: +1234567890", startX, startY + 60, fixedTextPaint)

        // Paint for questions and bubbles
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 14f
            isAntiAlias = true
        }
        val bubblePaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        val colWidth = (pageInfo.pageWidth - 2 * padding) / columns // Width for each column

        // Draw the questions on the PDF
        for (i in 1..numberOfQuestions) {
            val row = (i - 1) / columns
            val col = (i - 1) % columns

            val xPos = startX + col * colWidth
            val yPos = startY + fixedHeaderHeight + row * rowHeight

            canvas.drawText("Q$i:", xPos, yPos.toFloat(), textPaint)
            // Draw bubbles
            drawBubbles(canvas, xPos + 30, yPos.toFloat(), bubblePaint) // Adjusted xPos for bubbles
        }

        myPdfDocument.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }

    private fun drawBubbles(canvas: Canvas, startX: Float, yPos: Float, bubblePaint: Paint) {
        val options = listOf("A", "B", "C", "D")
        val bubbleRadius = 8f
        val optionGap = 30 // Gap between each bubble option
        val bubbleYOffset = 0f // Offset to align bubbles properly with the question text

        for ((index, option) in options.withIndex()) {
            val xPos = startX + index * optionGap // Adjusted X position for bubbles
            canvas.drawCircle(xPos, yPos + bubbleYOffset, bubbleRadius, bubblePaint)
            canvas.drawText(option, xPos - 5, yPos + bubbleYOffset + 20, bubblePaint)
        }
    }
}
