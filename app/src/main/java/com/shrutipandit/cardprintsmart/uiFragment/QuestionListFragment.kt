package com.shrutipandit.cardprintsmart.uiFragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.adapter.QuestionListAdapter
import com.shrutipandit.cardprintsmart.databinding.FragmentQuestionListBinding

class QuestionListFragment : Fragment(R.layout.fragment_question_list) {
    private lateinit var binding: FragmentQuestionListBinding
    private val items = mutableListOf<String>() // To hold the titles and descriptions
    private lateinit var adapter: QuestionListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuestionListBinding.bind(view)

        // Setup the adapter for the ListView
        adapter = QuestionListAdapter(requireContext(), items)
        binding.qListView.adapter = adapter

        loadData()

        binding.addTittleBtn.setOnClickListener {
            showAddTitleDialog()
        }

        binding.qListView.setOnItemClickListener { _, _, position, _ ->
            val action = QuestionListFragmentDirections.actionQuestionListFragmentToQuestionDetailsFragment()
            findNavController().navigate(action)
        }

        binding.qListView.setOnItemLongClickListener { _, _, position, _ ->
            showEditDeleteDialog(position)
            true
        }
    }

    private fun showAddTitleDialog() {
        // Inflate the custom dialog layout
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.addtittledialog, null)

        // Create the AlertDialog
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Add Title and Description")
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Bind the EditText and Button from the dialog layout
        val editTextTitle = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val editTextDescription = dialogView.findViewById<EditText>(R.id.editTextDescription)
        val submitButton = dialogView.findViewById<Button>(R.id.buttonSubmit)

        // Handle the submit button click
        submitButton.setOnClickListener {
            val title = editTextTitle.text.toString()
            val description = editTextDescription.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val entry = "$title: $description"
                items.add(entry) // Combine title and description
                adapter.notifyDataSetChanged() // Notify adapter to refresh ListView
                saveData() // Save the updated list to SharedPreferences
                dialog.dismiss() // Dismiss dialog
            } else {
                // Show error message (you can add Toast or other UI feedback)
            }
        }

        dialog.show()
    }

    private fun showEditDeleteDialog(position: Int) {
        val options = arrayOf("Edit", "Delete")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select an option")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> showEditDialog(position) // Edit option
                    1 -> deleteItem(position) // Delete option
                }
            }
            .show()
    }

    fun showEditDialog(position: Int) {
        // Inflate the custom dialog layout
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.addtittledialog, null)

        // Create the AlertDialog
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Edit Title and Description")
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Bind the EditText and Button from the dialog layout
        val editTextTitle = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val editTextDescription = dialogView.findViewById<EditText>(R.id.editTextDescription)
        val submitButton = dialogView.findViewById<Button>(R.id.buttonSubmit)

        // Populate the dialog with the existing data
        val currentEntry = items[position].split(": ")
        editTextTitle.setText(currentEntry[0])
        editTextDescription.setText(currentEntry[1])

        // Handle the submit button click
        submitButton.setOnClickListener {
            val title = editTextTitle.text.toString()
            val description = editTextDescription.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                items[position] = "$title: $description" // Update the entry
                adapter.notifyDataSetChanged() // Notify adapter to refresh ListView
                saveData() // Save the updated list to SharedPreferences
                dialog.dismiss() // Dismiss dialog
            } else {
                Toast.makeText(requireContext(), "Both fields must be filled", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    fun deleteItem(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Yes") { _, _ ->
                items.removeAt(position) // Remove the item from the list
                adapter.notifyDataSetChanged() // Notify adapter to refresh ListView
                saveData() // Save the updated list to SharedPreferences
                Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }


    private fun saveData() {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("items", items.toSet()) // Save as a Set to avoid duplicates
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val savedItems = sharedPreferences.getStringSet("items", emptySet()) ?: emptySet()
        items.clear()
        items.addAll(savedItems) // Load saved items into the list
    }
}