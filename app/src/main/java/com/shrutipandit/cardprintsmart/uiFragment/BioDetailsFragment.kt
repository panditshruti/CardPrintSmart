package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentBioDetailsBinding


class BioDetailsFragment : Fragment(R.layout.fragment_bio_details) {
private lateinit var binding:FragmentBioDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBioDetailsBinding.bind(view)

    }




}
