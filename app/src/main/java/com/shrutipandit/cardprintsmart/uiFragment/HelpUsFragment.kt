package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentHelpUsBinding


class HelpUsFragment : Fragment(R.layout.fragment_help_us) {
private lateinit var binding:FragmentHelpUsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHelpUsBinding.bind(view)



    }

}
