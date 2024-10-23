package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentQuestionMakerDetailsBinding

class QuestionMakerDetailsFragment : Fragment(R.layout.fragment_question_maker_details) {
    private lateinit var binding: FragmentQuestionMakerDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        binding = FragmentQuestionMakerDetailsBinding.bind(view)

    }
}