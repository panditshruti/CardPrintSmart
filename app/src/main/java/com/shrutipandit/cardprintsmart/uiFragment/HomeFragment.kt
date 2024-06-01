package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentHomeBinding


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding:FragmentHomeBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.marriage.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToMarriageBioDataFragment()
            findNavController().navigate(action)

        }
        binding.omrsheet.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToOMRSheetFragment()
            findNavController().navigate(action)

        }

    }

}
