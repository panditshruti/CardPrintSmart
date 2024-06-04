package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentResumeBinding


class ResumeFragment : Fragment(R.layout.fragment_resume) {
    private lateinit var binding:FragmentResumeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResumeBinding.bind(view)




    }


}
