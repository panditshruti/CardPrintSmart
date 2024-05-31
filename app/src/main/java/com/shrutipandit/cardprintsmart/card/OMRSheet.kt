import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.ByteArrayOutputStream

class OMRSheet {

    fun generatePdf(context: Context, numberOfQuestions: Int, paperSize: String): ByteArray {
        val myPdfDocument = PdfDocument()
        val pageInfo = when (paperSize) {
            "A3" -> PdfDocument.PageInfo.Builder(842, 1191, 1).create() // A3 size
            "A5" -> PdfDocument.PageInfo.Builder(420, 595, 1).create() // A5 size
            else -> PdfDocument.PageInfo.Builder(595, 842, 1).create() // Default A4 size
        }
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
        val startX = 40f
        val startY = 40f

        // Fixed text
        canvas.drawText("Name: John Doe", startX, startY, fixedTextPaint)
        canvas.drawText("Class: 10th Grade", startX, startY + 20, fixedTextPaint)
        canvas.drawText("Roll Number: 123456", startX, startY + 40, fixedTextPaint)
        canvas.drawText("Mobile Number: +1234567890", startX, startY + 60, fixedTextPaint)

        // Calculate the number of columns and rows based on the paper size
        val columns = when (paperSize) {
            "A3" -> 5
            "A5" -> 2
            else -> 3 // Default A4 columns
        }
        val rows = Math.ceil(numberOfQuestions / columns.toDouble()).toInt()

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

        val optionGap = 30 // Gap between options
        val rowHeight = 40 // Height for each row of questions
        val colWidth = (pageInfo.pageWidth - 80) / columns // Width for each column

        // Draw the questions on the PDF
        for (i in 1..numberOfQuestions) {
            val row = (i - 1) / columns
            val col = (i - 1) % columns

            val xPos = startX + col * colWidth
            val yPos = startY + 100 + row * rowHeight

            canvas.drawText("Q$i:", xPos, yPos.toFloat(), textPaint)
            // Draw bubbles
            drawBubbles(canvas, xPos, yPos.toFloat(), bubblePaint)
        }

        myPdfDocument.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }

    private fun drawBubbles(canvas: Canvas, startX: Float, yPos: Float, bubblePaint: Paint) {
        val options = listOf("A", "B", "C", "D")
        val bubbleRadius = 10f
        val optionGap = 40 // Gap between each bubble option
        val bubbleYOffset = 5f // Offset to align bubbles properly with the question text

        for ((index, option) in options.withIndex()) {
            val xPos = startX + 30 + index * optionGap // Adjusted X position for bubbles
            canvas.drawCircle(xPos, yPos + bubbleYOffset, bubbleRadius, bubblePaint)
            canvas.drawText(option, xPos - 5, yPos + bubbleYOffset + 20, bubblePaint)
        }
    }
}
