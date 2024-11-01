package com.shrutipandit.cardprintsmart.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shrutipandit.cardprintsmart.databinding.ItemPdfPageBinding

class QuestionMakerAdapter : RecyclerView.Adapter<QuestionMakerAdapter.QuestionMakerViewHolder>() {

    private val bitmapList = mutableListOf<Bitmap>()

    // Function to update the list of bitmaps
    fun setBitmapList(newBitmaps: List<Bitmap>) {
        bitmapList.clear()
        bitmapList.addAll(newBitmaps)
        notifyDataSetChanged() // Notify that the entire data set has changed
    }

    // Inflating the view and creating the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionMakerViewHolder {
        val binding = ItemPdfPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionMakerViewHolder(binding)
    }

    // Binding each bitmap to its corresponding ImageView in the ViewHolder
    override fun onBindViewHolder(holder: QuestionMakerViewHolder, position: Int) {
        holder.bind(bitmapList[position])
    }

    // Returning the total number of items in the bitmap list
    override fun getItemCount(): Int = bitmapList.size

    // ViewHolder class to hold and bind the bitmap to ImageView
    inner class QuestionMakerViewHolder(private val binding: ItemPdfPageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bitmap: Bitmap) {
            binding.imageView.setImageBitmap(bitmap)
        }
    }
}
