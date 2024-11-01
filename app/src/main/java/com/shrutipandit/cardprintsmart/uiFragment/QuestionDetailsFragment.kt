package com.shrutipandit.cardprintsmart.uiFragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shrutipandit.cardprintsmart.AppDatabase
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.adapter.QuestionsAdapter
import com.shrutipandit.cardprintsmart.db.Question
import com.shrutipandit.cardprintsmart.databinding.FragmentQuestionDetailsBinding
import kotlinx.coroutines.launch

class QuestionDetailsFragment : Fragment(R.layout.fragment_question_details) {
    private lateinit var binding: FragmentQuestionDetailsBinding
    private val questionsList = mutableListOf<Question>()
    private lateinit var questionsAdapter: QuestionsAdapter
    private lateinit var db: AppDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        binding = FragmentQuestionDetailsBinding.bind(view)

        db = AppDatabase.getDatabase(requireContext())

        // Setup RecyclerView
        questionsAdapter = QuestionsAdapter(questionsList)
        binding.recyclerViewQuestions.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewQuestions.adapter = questionsAdapter

        binding.headingBtn.setOnClickListener {
            showHeadingDialog()
        }

        binding.questionBtn.setOnClickListener {
            showQuestionDialog()
        }

        binding.savePdfBtn.setOnClickListener {
            saveToPdf()
        }

        loadSavedData()
    }

    private fun loadSavedData() {
        lifecycleScope.launch {
            questionsList.clear()
//            questionsList.addAll(db.questionDao().getAllQuestions())
            questionsAdapter.notifyDataSetChanged()
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
            .setTitle("Create Question")
            .setView(dialogView)
            .setCancelable(true)
            .create()

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

            // Create new Question object
            val newQuestion = Question(text = question, answer = answer, options = options)

            // Add the new question to the database
            lifecycleScope.launch {
//                db.questionDao().insert(newQuestion)
                questionsList.add(newQuestion)
                questionsAdapter.notifyDataSetChanged() // Update the adapter

                // Show a toast to indicate the question has been saved
                Toast.makeText(requireContext(), "Question Saved", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun saveToPdf() {
        // Implement your PDF saving logic here
        Toast.makeText(requireContext(), "PDF saved!", Toast.LENGTH_SHORT).show()
    }
}