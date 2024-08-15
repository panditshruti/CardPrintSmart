package com.shrutipandit.cardprintsmart.card

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument as ITextPdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class PdfandPrint {

    fun savePdf(context: Context, pdfBytes: ByteArray) {
        // Create a directory for the PDF file
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "PDFs")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        // Create the PDF file
        val file = File(directory, "MarriageBioData.pdf")

        // Write the byte array to the file
        try {
            FileOutputStream(file).use { fos ->
                fos.write(pdfBytes)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

//    fun printPdf(context: Context) {
//        val file = createPdf(context)
//
//        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
//        val printAdapter = object : PrintDocumentAdapter() {
//            override fun onWrite(
//                pages: Array<out PdfDocument.PageRange>,
//                destination: ParcelFileDescriptor,
//                cancellationSignal: CancellationSignal,
//                callback: PrintDocumentAdapter.WriteResultCallback
//            ) {
//                try {
//                    FileInputStream(file).use { inputStream ->
//                        FileOutputStream(destination.fileDescriptor).use { outputStream ->
//                            val buf = ByteArray(1024)
//                            var bytesRead: Int
//                            while (inputStream.read(buf).also { bytesRead = it } > 0) {
//                                outputStream.write(buf, 0, bytesRead)
//                            }
//                        }
//                    }
//                    callback.onWriteFinished(arrayOf(PrintDocumentAdapter.PagRange.ALL_PAGES))
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                    callback.onWriteFailed(e.message)
//                }
//            }
//
//            override fun onLayout(
//                oldAttributes: PrintAttributes,
//                newAttributes: PrintAttributes,
//                cancellationSignal: CancellationSignal,
//                callback: PrintDocumentAdapter.LayoutResultCallback,
//                extras: Bundle?
//            ) {
//                callback.onLayoutFinished(
//                    PrintDocumentInfo.Builder(file.name)
//                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
//                        .build(),
//                    true
//                )
//            }
//
//            override fun onWrite(
//                p0: Array<out PageRange>?,
//                p1: ParcelFileDescriptor?,
//                p2: CancellationSignal?,
//                p3: WriteResultCallback?
//            ) {
//                TODO("Not yet implemented")
//            }
//        }
//
//        printManager.print("Document", printAdapter, null)
//    }

    fun createPdf(context: Context): File {
        val file = File(context.getExternalFilesDir(null), "sample.pdf")
        val pdfWriter = PdfWriter(FileOutputStream(file))
        val pdfDocument = ITextPdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        document.add(
            Paragraph("Hello World!")
                .setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(24f))

        document.close()
        return file
    }
}
