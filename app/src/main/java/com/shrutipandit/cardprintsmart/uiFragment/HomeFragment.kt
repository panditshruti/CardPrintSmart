package com.shrutipandit.cardprintsmart.uiFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
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
        binding.idCard.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToIDCardFragment()
            findNavController().navigate(action)
        }
        binding.resume.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToResumeFragment()
            findNavController().navigate(action)

        }
        binding.bioDetails.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToBioDetailsFragment()
            findNavController().navigate(action)

        }
        binding.examCopy.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToExamCopyFragment()
            findNavController().navigate(action)
        }
        binding.paymentBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToExamCopyFragment()
            findNavController().navigate(action)
        }
         binding.mcqCheker.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToMCQCheckerFragment()
            findNavController().navigate(action)
        }
        binding.questionMaker.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToQuestionMakerListFragment2()
            findNavController().navigate(action)
        }
        binding.marriagePamplet.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToMarriagePampletFragment()
            findNavController().navigate(action)
        }
        binding.applicationMaker.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToApplicationMakerFragment()
            findNavController().navigate(action)
        }
        binding.aayPramanPatra.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAayPramanPatraFragment()
            findNavController().navigate(action)
        }

    }
}