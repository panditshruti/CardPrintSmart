package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.card.MarriageBioData
import com.shrutipandit.cardprintsmart.databinding.FragmentDemoMarriageCardBinding

class DemoMarriageCardFragment : Fragment() {
private lateinit var binding:FragmentDemoMarriageCardBinding
    private val marriageBioData = MarriageBioData()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDemoMarriageCardBinding.bind(view)


        marriageBioData.templet1(requireContext())



    }


}
