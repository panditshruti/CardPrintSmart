package com.shrutipandit.cardprintsmart.uiFragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentQuestionDetailsBinding

class QuestionDetailsFragment : Fragment(R.layout.fragment_question_details) {
    private lateinit var binding: FragmentQuestionDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuestionDetailsBinding.bind(view)

        binding.headingBtn.setOnClickListener {
            showHeadingDialog()
        }

        binding.questionBtn.setOnClickListener {
            showQuestionDialog()
        }
    }

    private fun showHeadingDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_headingq_page, null)
        val schoolNameEditText = dialogView.findViewById<EditText>(R.id.editTextSchoolName)
        val classEditText = dialogView.findViewById<EditText>(R.id.editTextClass)
        val totalMarksEditText = dialogView.findViewById<EditText>(R.id.editTextTotalMarks)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Enter Heading Details")
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialogView.findViewById<Button>(R.id.buttonSubmit).setOnClickListener {
            val schoolName = schoolNameEditText.text.toString()
            val className = classEditText.text.toString()
            val totalMarks = totalMarksEditText.text.toString()

            if (schoolName.isNotEmpty() && className.isNotEmpty() && totalMarks.isNotEmpty()) {
                binding.headingBtn.text = "$schoolName, Class: $className, Total Marks: $totalMarks"
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun showQuestionDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_question, null)
        val questionEditText = dialogView.findViewById<EditText>(R.id.editTextQuestion)
        val answerEditText = dialogView.findViewById<EditText>(R.id.editTextAnswer)
        val optionsEditText = dialogView.findViewById<EditText>(R.id.editTextOptions)
        val isSubjectiveButton = dialogView.findViewById<Button>(R.id.buttonSubjective)
        val isObjectiveButton = dialogView.findViewById<Button>(R.id.buttonObjective)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Set initial visibility
        answerEditText.visibility = View.GONE
        optionsEditText.visibility = View.GONE

        isSubjectiveButton.setOnClickListener {
            answerEditText.visibility = View.VISIBLE
            optionsEditText.visibility = View.GONE
        }

        isObjectiveButton.setOnClickListener {
            answerEditText.visibility = View.GONE
            optionsEditText.visibility = View.VISIBLE
        }

        dialogView.findViewById<Button>(R.id.buttonSubmitQuestion).setOnClickListener {
            val question = questionEditText.text.toString()
            val answer = answerEditText.text.toString()
            val options = optionsEditText.text.toString()

            if (question.isNotEmpty()) {
                // Handle saving question based on its type (objective/subjective)
                if (isObjectiveButton.isPressed && options.isNotEmpty()) {
                    // Save objective question
                    Toast.makeText(requireContext(), "Objective Question Saved", Toast.LENGTH_SHORT).show()
                } else if (isSubjectiveButton.isPressed && answer.isNotEmpty()) {
                    // Save subjective question
                    Toast.makeText(requireContext(), "Subjective Question Saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Question cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

}
