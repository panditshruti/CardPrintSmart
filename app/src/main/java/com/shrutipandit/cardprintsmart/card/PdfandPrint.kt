package com.shrutipandit.cardprintsmart.card

import android.content.Context
import android.os.Environment
import java.io.File
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
}
