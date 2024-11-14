package com.shrutipandit.cardprintsmart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.shrutipandit.cardprintsmart.R
import com.shrutipandit.cardprintsmart.db.PageSummary
import com.shrutipandit.cardprintsmart.uiFragment.QuestionListFragment

class QuestionListAdapter(context: Context, private val items: List<PageSummary>) :
    ArrayAdapter<PageSummary>(context, R.layout.list_item_question, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_question, parent, false)

        val textViewItem = view.findViewById<TextView>(R.id.textViewItem)
        val imageEdit = view.findViewById<ImageView>(R.id.imageEdit)
        val imageDelete = view.findViewById<ImageView>(R.id.imageDelete)

        textViewItem.text = items[position].title + " "
        textViewItem.append( items[position].description)

        imageEdit.setOnClickListener {
            (context as? QuestionListFragment)?.showEditDialog(position)
        }

        imageDelete.setOnClickListener {
            (context as? QuestionListFragment)?.deleteItem(position)
        }

        return view
    }
}