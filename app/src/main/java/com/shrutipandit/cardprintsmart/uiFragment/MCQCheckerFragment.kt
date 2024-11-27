package com.shrutipandit.cardprintsmart.uiFragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentMCQCheckerBinding
import java.io.InputStream


class MCQCheckerFragment : Fragment(R.layout.fragment_m_c_q_checker) {

    private lateinit var binding: FragmentMCQCheckerBinding
    private var answerSheetImage: Bitmap? = null
    private var omrSheetImage: Bitmap? = null
    private var answerKey: Map<Int, Char> = emptyMap() // Parsed from the Answer Sheet

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMCQCheckerBinding.bind(view)

//        // Initialize OpenCV
//        if (OpenCVLoader.initDebug()) {
//            // OpenCV successfully initialized
//            println("OpenCV Initialization Successful")
//        } else {
//            // OpenCV initialization failed
//            println("OpenCV Initialization Failed")
//        }

        // Answer Sheet Image Picker
        val answerSheetPicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                answerSheetImage = getBitmapFromUri(it)
                processAnswerSheet(answerSheetImage!!)
            }
        }

        // OMR Sheet Image Picker
        val omrSheetPicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                omrSheetImage = getBitmapFromUri(it)
            }
        }

        binding.btnUploadAnswerSheet.setOnClickListener {
            answerSheetPicker.launch("image/*")
        }

        binding.btnUploadOmrSheet.setOnClickListener {
            omrSheetPicker.launch("image/*")
        }

        binding.btnCheck.setOnClickListener {
            if (answerSheetImage != null && omrSheetImage != null) {
                val studentAnswers = processOMRSheet(omrSheetImage!!)
                val (correct, incorrect) = compareAnswers(answerKey, studentAnswers)
                binding.tvResults.text = "Correct: $correct\nIncorrect: $incorrect"
            } else {
                binding.tvResults.text = "Please upload both images!"
            }
        }
    }

    // Convert URI to Bitmap
    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }

    // Process Answer Sheet (Extract correct answers using OCR)
    private fun processAnswerSheet(bitmap: Bitmap) {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                answerKey = parseAnswerKey(visionText.text)
                binding.tvResults.text = "Answer Sheet Uploaded Successfully"
            }
            .addOnFailureListener {
                binding.tvResults.text = "Failed to process Answer Sheet"
            }
    }

    // Process OMR Sheet (Extract marked answers)
    private fun processOMRSheet(bitmap: Bitmap): Map<Int, Char> {
        // Use OpenCV to detect filled bubbles in the OMR sheet
        // TODO: Implement OpenCV logic for detecting filled answers
        return mapOf(
            1 to 'B',
            2 to 'A',
            3 to 'C',
            4 to 'D',
            5 to 'A'
        ) // Replace this dummy data with actual detection logic
    }

    // Parse OCR Text from Answer Sheet into a Map
    private fun parseAnswerKey(input: String): Map<Int, Char> {
        val keyMap = mutableMapOf<Int, Char>()
        input.split("\n").forEach { line ->
            val parts = line.split(",")
            if (parts.size == 2) {
                val questionNumber = parts[0].trim().toIntOrNull()
                val correctAnswer = parts[1].trim().firstOrNull()
                if (questionNumber != null && correctAnswer != null) {
                    keyMap[questionNumber] = correctAnswer.uppercaseChar()
                }
            }
        }
        return keyMap
    }

    // Compare extracted answers from OMR sheet with the answer key
    private fun compareAnswers(
        answerKey: Map<Int, Char>,
        studentAnswers: Map<Int, Char>
    ): Pair<Int, Int> {
        var correct = 0
        var incorrect = 0
        answerKey.forEach { (question, correctAnswer) ->
            if (studentAnswers[question] == correctAnswer) {
                correct++
            } else {
                incorrect++
            }
        }
        return Pair(correct, incorrect)
    }
}
