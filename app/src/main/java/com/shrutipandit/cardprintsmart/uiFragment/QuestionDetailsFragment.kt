package com.shrutipandit.cardprintsmart.uiFragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentQuestionDetailsBinding
import com.shrutipandit.cardprintsmart.databinding.FragmentQuestionListBinding

class QuestionDetailsFragment : Fragment(R.layout.fragment_question_details) {
        private lateinit var binding: FragmentQuestionDetailsBinding


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding = FragmentQuestionDetailsBinding.bind(view)


    }


}
