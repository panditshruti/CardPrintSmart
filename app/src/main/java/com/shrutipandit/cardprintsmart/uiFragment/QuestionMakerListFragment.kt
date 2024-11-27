package com.shrutipandit.cardprintsmart.uiFragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shrutipandit.cardprintsmart.room.AppDatabase
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.adapter.QuestionListAdapter
import com.shrutipandit.cardprintsmart.databinding.FragmentQuestionMakerListBinding
import com.shrutipandit.cardprintsmart.db.PageContent
import com.shrutipandit.cardprintsmart.db.Question
import com.shrutipandit.cardprintsmart.viewModel.PageViewModel
import kotlinx.coroutines.launch

class QuestionMakerListFragment : Fragment(R.layout.fragment_question_maker_list) {
    private lateinit var binding: FragmentQuestionMakerListBinding
    private val items = mutableListOf<String>() // To hold the titles and descriptions
    private lateinit var adapter: QuestionListAdapter
    private lateinit var pageViewModel: PageViewModel



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuestionMakerListBinding.bind(view)

        pageViewModel = ViewModelProvider(this)[PageViewModel::class.java]

        pageViewModel.pageSummaries.observe(viewLifecycleOwner) { summaries ->
            adapter = QuestionListAdapter(requireContext(), summaries)
            binding.qListView.adapter = adapter
        }



        binding.addTittleBtn.setOnClickListener {
            showAddTitleDialog()
        }

        binding.qListView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = pageViewModel.pageSummaries.value?.get(position)

            // Navigate to the details fragment with arguments
            val action = QuestionMakerListFragmentDirections.actionQuestionMakerListFragmentToQuestionMakerDetailsFragment(
                selectedItem?.title.toString(),selectedItem?.description.toString(),selectedItem?.id!!
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
                addQuestionOnRoomDataBase(title,description,emptyList())
                adapter.notifyDataSetChanged()
                dialog.dismiss() // Dismiss dialog
            } else {
                // Show error message (you can add Toast or other UI feedback)
            }
        }

        dialog.show()
    }


    private fun addQuestionOnRoomDataBase(title:String, desc:String, question:List<Question>){
        val pageContent = PageContent(0,title,desc,question)
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
                Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }


}