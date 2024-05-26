package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentDemoOMRSheetBinding

class DemoOMRSheetFragment : Fragment(R.layout.fragment_demo_o_m_r_sheet) {

    private lateinit var binding: FragmentDemoOMRSheetBinding
    private val args: DemoOMRSheetFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDemoOMRSheetBinding.bind(view)

        val numberOfQuestions = args.numberOfQuestions
        generateOMRSheet(numberOfQuestions)
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
}
