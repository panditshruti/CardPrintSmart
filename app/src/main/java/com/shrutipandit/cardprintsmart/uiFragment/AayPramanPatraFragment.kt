package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentAayPramanPatraBinding


class AayPramanPatraFragment : Fragment(R.layout.fragment_aay_praman_patra) {
private lateinit var binding:FragmentAayPramanPatraBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAayPramanPatraBinding.bind(view)


    }

}
