package com.shrutipandit.cardprintsmart
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
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

        val bioDatatv = TextView(this)
        bioDatatv.text= "MarriageBioData"
        linearLayout.addView(bioDatatv)


        val marriageArrayList = arrayListOf(
            "Name             :",
//           " "Date Of Birth    :",
//            "Birth Place      :",
//            "Religion         :",
//            "Caste            :","
//            "Height           :",
//            "Blood Group      :",
//            "Complexion       :",
//            "Education        :",
//            "10 Marks         :",
//            "12 Marks         :",
//            "Post             :"
//            "District         :",
//            "State            :",
//            "Fimaly Information",
//            "Father Name      :",
//            "Occupation       :",
//            "Mother Name      :",
//            "Occupation       :",
//            "Sister           :",
//            "Brother          :",
//            "Contact Information",
//            "Address          :",
//            "Contact No-      :"
        )

        val editTextList = mutableListOf<EditText>()

        // Create EditText for each item in marriageArrayList
        for (item in marriageArrayList) {
            val editText = EditText(this)
            editText.hint = item
            linearLayout.addView(editText)
            editTextList.add(editText)
        }

        // Create Button for submission
        val submitButton = Button(this)
        submitButton.text = "Submit"
        submitButton.setBackgroundColor(Color.GREEN)
        submitButton.setTextColor(Color.BLACK) // Set text color

        // Set layout params with gravity set to center and margins
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.CENTER
        params.setMargins(0, 20, 0, 0) // Set margins
        submitButton.layoutParams = params // Set the layout params for the submit button

        // Add click listener for the submit button
        submitButton.setOnClickListener {
            // Get text from EditText fields
            val textList = mutableListOf<String>()
            for (editText in editTextList) {
                val text = editText.text.toString()
                if (text.isBlank()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                textList.add(text)
            }

            // Generate PDF
            marriageBioData.templet1(this)

            // Show toast
            Toast.makeText(this, "Marriage BioData Created", Toast.LENGTH_SHORT).show()
        }


        // Add the submit button to the main linear layout
        linearLayout.addView(submitButton)
    }
}
