package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentIDCardBinding


class IDCardFragment : Fragment(R.layout.fragment_i_d_card) {
private lateinit var binding: FragmentIDCardBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIDCardBinding.bind(view)




    }

}
