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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shrutipandit.cardprintsmart.AppDatabase
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.adapter.QuestionListAdapter
import com.shrutipandit.cardprintsmart.databinding.FragmentQuestionListBinding
import com.shrutipandit.cardprintsmart.databinding.FragmentQuestionMakerListBinding
import com.shrutipandit.cardprintsmart.db.PageContent
import com.shrutipandit.cardprintsmart.db.Question
import kotlinx.coroutines.launch

class QuestionMakerListFragment : Fragment(R.layout.fragment_question_maker_list) {
    private lateinit var binding: FragmentQuestionMakerListBinding
    private val items = mutableListOf<String>() // To hold the titles and descriptions
    private lateinit var adapter: QuestionListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuestionMakerListBinding.bind(view)

        // Setup the adapter for the ListView
        adapter = QuestionListAdapter(requireContext(), items)
        binding.qListView.adapter = adapter
        loadData()

        binding.addTittleBtn.setOnClickListener {
            showAddTitleDialog()
        }

        binding.qListView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]
            val title = selectedItem.split(": ")[0] // Extract title
            val description = selectedItem.split(": ")[1] // Extract description

            // Navigate to the details fragment with arguments
            val action = QuestionMakerListFragmentDirections.actionQuestionMakerListFragmentToQuestionMakerDetailsFragment(
                title,description,position
            )
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
                addQuestionOnRoomDataBase(emptyList())
                dialog.dismiss() // Dismiss dialog
            } else {
                // Show error message (you can add Toast or other UI feedback)
            }
        }

        dialog.show()
    }

    private fun addQuestionOnRoomDataBase(question:List<Question>){
        val pageContent = PageContent(0,question)
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            db.pageContentDao().insertPageContent(pageContent)
        }

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
        editTextDescription.setText(currentEntry.getOrNull(1) ?: "") // Avoid IndexOutOfBoundsException

        // Handle the submit button click
        submitButton.setOnClickListener {
            val title = editTextTitle.text.toString()
            val description = editTextDescription.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                items[position] = "$title: $description" // Update the entry in the correct position
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
        val sharedPreferences = requireContext().getSharedPreferences("MyPres", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("items", items.toSet()) // Save as a Set to avoid duplicates
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = requireContext().getSharedPreferences("MyPres", Context.MODE_PRIVATE)
        val savedItems = sharedPreferences.getStringSet("items", emptySet()) ?: emptySet()
        items.clear()
        items.addAll(savedItems) // Load saved items into the list
    }
}