import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import com.shrutipandit.cardprintsmart.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class OMRSheet {

    fun omrSheetDetails(
        context: Context,
        name: String,
        rollNo: String,
        className: String,
        phoneNumber: String
    ) {
        val myPdfDocument = PdfDocument()
        val myPageInfo1 = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val myPage1: PdfDocument.Page = myPdfDocument.startPage(myPageInfo1)
        val canvas = myPage1.canvas

        val textPaint = Paint().apply {
            color = Color.rgb(0, 0, 0)
            textSize = 12f
            letterSpacing = 0.05f
        }

        // Draw the text on the PDF
        canvas.drawText("Name: $name", 50f, 50f, textPaint)
        canvas.drawText("Roll No: $rollNo", 50f, 70f, textPaint)
        canvas.drawText("Class: $className", 50f, 90f, textPaint)
        canvas.drawText("Phone Number: $phoneNumber", 50f, 110f, textPaint)

        myPdfDocument.finishPage(myPage1)

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "OMR_Sheet.pdf"
        )

        try {
            FileOutputStream(file).use { fos ->
                myPdfDocument.writeTo(fos)
                Toast.makeText(context, "PDF generated and saved to Downloads", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error generating PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            myPdfDocument.close()
        }
    }

    fun generatePdf(context: Context): ByteArray {
        val myPdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page: PdfDocument.Page = myPdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // Add your PDF content here

        myPdfDocument.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        myPdfDocument.writeTo(outputStream)
        myPdfDocument.close()

        return outputStream.toByteArray()
    }
}
