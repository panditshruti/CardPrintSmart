package com.shrutipandit.cardprintsmart.uiFragment

import android.content.Intent
import android.net.Uri
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


        binding.call.setOnClickListener {
            helpCenter()
        }


    }
    private fun helpCenter() {
        val phoneNumber = "+7739717389"
        val message =
            "Hello, I there is a problem in ADCA Computer Course. Can you please help me troubleshoot it?"
        val encodedMessage = Uri.encode(message)
        val uri = Uri.parse("https://wa.me/$phoneNumber?text=$encodedMessage")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.whatsapp")
        startActivity(intent)

    }
    }
