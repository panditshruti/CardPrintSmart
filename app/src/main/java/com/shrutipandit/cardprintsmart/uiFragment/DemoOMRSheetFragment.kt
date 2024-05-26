package com.shrutipandit.cardprintsmart.uiFragment

import OMRSheet
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentDemoOMRSheetBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DemoOMRSheetFragment : Fragment(R.layout.fragment_demo_o_m_r_sheet) {

    private lateinit var binding: FragmentDemoOMRSheetBinding
    private val args: DemoOMRSheetFragmentArgs by navArgs()
    private  var omrSheet = OMRSheet()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDemoOMRSheetBinding.bind(view)

        val numberOfQuestions = args.numberOfQuestions
        generateOMRSheet(numberOfQuestions)

        omrSheet.omrSheetDetails(requireContext(),"shruti","2","11","7739717389")


    }

    private fun generateOMRSheet(numberOfQuestions: Int) {
        binding.questionsContainer.removeAllViews()

        for (i in 1..numberOfQuestions) {
            val questionTextView = TextView(requireContext()).apply {
                text = "Question $i:"
                textSize = 18f
                setPadding(0, 10, 0, 10)
            }
            binding.questionsContainer.addView(questionTextView)

            val radioGroup = RadioGroup(requireContext()).apply {
                orientation = RadioGroup.HORIZONTAL
            }

            val options = arrayOf("A", "B", "C", "D")
            for (option in options) {
                val radioButton = RadioButton(requireContext()).apply {
                    text = option
                    id = View.generateViewId()
                }
                radioGroup.addView(radioButton)
            }
            binding.questionsContainer.addView(radioGroup)
        }
    }

    private fun savePdf(pdfBytes: ByteArray) {
        val directory = File(requireContext().getExternalFilesDir(null), "PDFs")
        if (!directory.exists() && !directory.mkdirs()) {
            return
        }

        val file = File(directory, "omr_sheet.pdf")
        try {
            FileOutputStream(file).use { fos ->
                fos.write(pdfBytes)
                Toast.makeText(requireContext(), "PDF saved successfully", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "PDF failed successfully", Toast.LENGTH_SHORT).show()

        }
    }

}
