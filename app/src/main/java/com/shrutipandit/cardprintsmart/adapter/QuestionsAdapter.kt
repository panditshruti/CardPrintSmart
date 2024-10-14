package com.shrutipandit.cardprintsmart.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.db.Question

class QuestionsAdapter(private val questions: MutableList<Question>) : RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewQuestion: TextView = itemView.findViewById(R.id.textViewQuestion)
        private val textViewAnswer: TextView = itemView.findViewById(R.id.textViewAnswer)
        private val textViewOptions: TextView = itemView.findViewById(R.id.textViewOptions)

        fun bind(question: Question) {
            textViewQuestion.text = question.text // Use question.text instead of question.question
            textViewAnswer.text = if (question.answer.isNotEmpty()) question.answer else "N/A"
            textViewOptions.text = if (question.options.isNotEmpty()) question.options else "N/A"

            // Set visibility based on question type
            textViewAnswer.visibility = if (question.answer.isNotEmpty()) View.VISIBLE else View.GONE
            textViewOptions.visibility = if (question.options.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount(): Int = questions.size

    // Optionally, add a method to update the list and notify changes
    fun updateQuestions(newQuestions: List<Question>) {
        questions.clear()
        questions.addAll(newQuestions)
        notifyDataSetChanged()
    }
}