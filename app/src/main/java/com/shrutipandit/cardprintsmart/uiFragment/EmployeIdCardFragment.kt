package com.shrutipandit.cardprintsmart.uiFragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentEmployeIdCardBinding

class EmployeIdCardFragment : Fragment(R.layout.fragment_employe_id_card) {
    private lateinit var binding: FragmentEmployeIdCardBinding

    private var selectedImageUri: Uri? = null
    private val IMAGE_PICK_CODE = 1000

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEmployeIdCardBinding.bind(view)

        val linearLayout = binding.linearLayout

        val formFields = arrayListOf(
            "Title    :",
            "Name    :",
            "Description    :",
            "Class    :",
            "Roll NO.    :",
            "DOB    :",
            "Phone no.    :",
            "Email    :",
            "Address    :"
        )

        val editTexts = mutableListOf<TextInputEditText>()

        // Dynamically add TextInputEditText for each label
        for (label in formFields) {
            val textInputEditText = TextInputEditText(requireContext()).apply {
                hint = label
            }
            linearLayout.addView(
                textInputEditText,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            editTexts.add(textInputEditText)
        }

        // Button to choose an image from the gallery
        val chooseImageButton = Button(requireContext()).apply {
            text = "Choose Image"
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.BLUE)
            setOnClickListener {
                pickImageFromGallery()
            }
        }
        linearLayout.addView(chooseImageButton, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // Submit button to collect data and navigate to DemoIdCardFragment
        val submitButton = Button(requireContext()).apply {
            text = "Submit"
            setBackgroundColor(Color.RED)
            setOnClickListener {
                val data = editTexts.joinToString(",") { it.text.toString() }
                val imageUriString = selectedImageUri?.toString() ?: ""

                // Ensure the action ID is correctly referenced in the directions
                try {
//                    val action = EmployeIdCardFragmentDirections.actionEmployeIdCardFragmentToDemoIdCardFragment(data, imageUriString)
//                    findNavController().navigate(action)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Navigation Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        linearLayout.addView(submitButton, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    // Function to pick image from gallery
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            selectedImageUri = data?.data // Get selected image URI
        }
    }
}
