package com.shrutipandit.cardprintsmart.uiFragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.databinding.FragmentBioDetailsBinding
import com.shrutipandit.cardprintsmart.databinding.FragmentResumeBinding


class ResumeFragment : Fragment(R.layout.fragment_resume) {
    private lateinit var binding:FragmentResumeBinding
    private var selectedImageUri: Uri? = null
    private val IMAGE_PICK_CODE = 1000

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResumeBinding.bind(view)


                val linearLayout = binding.linearLayout

                val marriageArrayList = arrayListOf(
                    "Name                 :",
                    "Father's Name        :",
                    "Gender               :",
                    "Caste                :",
                    "Height               :",
                    "Blood Group          :",
                    "Date Of Birth        :",
                    "Mobile No-           :",
                    "Marital Status       :",
                    "Email Id             :",
                    "Religion             :",
                    "Nationality          :",
                    "Address              :",
                    "Languages Knows      :",
                    "Education            :",
                    "Computer Knowledge   :",
                    "Work                 :",
                    "Work Experience      :",
                    "Declaration          :",
                    "Place                :",
                    "Date                 :"
                )

                val editTexts = mutableListOf<TextInputEditText>()

                // Add TextInputEditTexts to the layout for each item in the marriageArrayList
                for (label in marriageArrayList) {
                    val textInputEditText = TextInputEditText(requireContext())
                    textInputEditText.hint = label
                    linearLayout.addView(textInputEditText, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    editTexts.add(textInputEditText)
                }

                // Add button to choose an image
                val chooseImageButton = Button(requireContext())
                chooseImageButton.text = "Choose Image"
                chooseImageButton.setBackgroundColor(Color.BLUE)
                chooseImageButton.setOnClickListener {
                    pickImageFromGallery()
                }
                linearLayout.addView(chooseImageButton, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                // Add the submit button at the end of the linear layout
                val submitButton = Button(requireContext())
                submitButton.text = "Submit"
                submitButton.setBackgroundColor(Color.RED)
                submitButton.setOnClickListener {
                    val data = editTexts.joinToString(",") { it.text.toString() }
                    val imageUriString = selectedImageUri?.toString() ?: ""
                    val action = ResumeFragmentDirections.actionResumeFragmentToDemoResumeFragment(data, imageUriString)
                    findNavController().navigate(action)
                }

                linearLayout.addView(submitButton, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }

            private fun pickImageFromGallery() {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, IMAGE_PICK_CODE)
            }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
                    selectedImageUri = data?.data
                }
            }
        }
