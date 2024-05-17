package com.shrutipandit.cardprintsmart

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.shrutipandit.cardprintsmart.card.MarriageBioData
import com.shrutipandit.cardprintsmart.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val marriageBioData = MarriageBioData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create a ScrollView
        val scrollView = ScrollView(this)
        setContentView(scrollView)

        // Create a LinearLayout to hold all views inside ScrollView
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setBackgroundColor(Color.WHITE) // Set background color

        // Set the main linear layout as the content view
        scrollView.addView(linearLayout)

        val marriageArrayList = arrayListOf(
            "Name             :",
            "Date Of Birth    :",
            "Birth Place      :",
            "Religion         :",
            "Caste            :",
            "Height           :",
            "Blood Group      :",
            "Complexion       :",
            "Education        :",
            "10 Marks         :",
            "12 Marks         :",
            "Post             :",
            "District         :",
            "State            :",
            "Family Information",
            "Father Name      :",
            "Occupation       :",
            "Mother Name      :",
            "Occupation       :",
            "Sister           :",
            "Brother          :",
            "Contact Information",
            "Address          :",
            "Contact No-      :"
        )

        val editTexts = mutableListOf<EditText>()

        // Add EditTexts to the layout for each item in the marriageArrayList
        for (label in marriageArrayList) {
            val editText = EditText(this)
            editText.hint = label
            linearLayout.addView(editText, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            editTexts.add(editText)
        }

        // Add the submit button at the end of the linear layout
        val submitButton = Button(this)
        submitButton.text = "Submit"
        submitButton.setOnClickListener {
            marriageBioData.populateDataFromEditTexts(editTexts)
            marriageBioData.templet1(this)
        }
        linearLayout.addView(submitButton, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
