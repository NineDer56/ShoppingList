package com.example.shoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R

class ShopItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textViewItem: TextView = itemView.findViewById(R.id.textViewItem)
    val textViewItemCount: TextView = itemView.findViewById(R.id.textViewItemCount)
}