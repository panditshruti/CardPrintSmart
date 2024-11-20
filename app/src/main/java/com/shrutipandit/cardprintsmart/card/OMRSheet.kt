import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.ByteArrayOutputStream

class OMRSheet {

    fun generatePdf(context: Context, numberOfQuestions: Int, paperSize: String): ByteArray {
        val fixedHeaderHeight = 150 // Height reserved for header information
        val rowHeight = 50 // Height for each row of questions
        val padding = 20 // Padding around the page
        val maxColumns = 4 // Number of columns to fit on the page

        // Define page size (A4 or others can be customized)
        val pageWidth = 595 // Default width for A4 in points
        val pageHeight = 842 // Default height for A4 in points

        // Calculate questions per page
        val questionsPerColumn = ((pageHeight - fixedHeaderHeight - padding) / rowHeight).toInt()
        val questionsPerPage = maxColumns * questionsPerColumn
        val totalPages = Math.ceil(numberOfQuestions / questionsPerPage.toDouble()).toInt()

        val myPdfDocument = PdfDocument()

        for (pageIndex in 0 until totalPages) {
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageIndex + 1).create()
            val page = myPdfDocument.startPage(pageInfo)
            val canvas: Canvas = page.canvas

            // Set background color
            canvas.drawColor(Color.WHITE)

            // Draw header on the first page only
            if (pageIndex == 0) drawHeader(canvas, padding, fixedHeaderHeight)

            // Draw questions
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

    private fun drawHeader(canvas: Canvas, padding: Int, headerHeight: Int) {
        val headerPaint = Paint().apply {
            color = Color.BLACK
            textSize = 14f
            isAntiAlias = true
        }
        val startX = padding.toFloat()
        var currentY = padding.toFloat()

        // Draw header fields
        canvas.drawText("Name: ___________________________", startX, currentY, headerPaint)
        currentY += 20
        canvas.drawText("Roll Number: ____________________", startX, currentY, headerPaint)
        currentY += 20
        canvas.drawText("Class: __________________________", startX, currentY, headerPaint)
        currentY += 20
        canvas.drawText("Mobile Number: __________________", startX, currentY, headerPaint)

        // Separator line
        currentY += 30
        canvas.drawLine(startX, currentY, canvas.width - padding.toFloat(), currentY, headerPaint)
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

        for (col in 0 until maxColumns) {
            var yPos = headerHeight + padding
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
}
