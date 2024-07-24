package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentAboutUsragmentBinding
import com.shrutipandit.cardprintsmart.databinding.FragmentHelpUsBinding


class AboutUsragment : Fragment(R.layout.fragment_about_usragment) {

    private lateinit var binding:FragmentAboutUsragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAboutUsragmentBinding.bind(view)



    }

    }
