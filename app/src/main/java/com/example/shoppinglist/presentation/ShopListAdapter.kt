package com.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopListViewHolder>() {

    private var _shopItems: List<ShopItem> = mutableListOf()
    var shopItems: List<ShopItem>
        get() = _shopItems.toList()
        set(value) {
            _shopItems = value
            notifyDataSetChanged()
        }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    private var count =0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        Log.d("ShopListAdapter", "onCreateViewHolder ${++count}")
        val layoutId = when(viewType) {
            VIEW_TYPE_ENABLED-> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED-> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type")
        }
        val view = LayoutInflater.from(parent.context).inflate(
            layoutId,
            parent,
            false
        )
        return ShopListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        val item = _shopItems[position]

        holder.apply {
            textViewItem.text = item.name
            textViewItemCount.text = item.count.toString()

            itemView.setOnLongClickListener {
                onItemClickListener?.onItemClick(item)
                true
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = _shopItems[position]
        return if (item.isEnable) VIEW_TYPE_ENABLED else VIEW_TYPE_DISABLED
    }

    override fun getItemCount() = _shopItems.size

//    Устанавливаем значения по умолчанию
//    override fun onViewRecycled(holder: ShopListViewHolder) {
//        super.onViewRecycled(holder)
//        holder.apply {
//            textViewItem.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
//        }
//    }

    class ShopListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewItem: TextView = itemView.findViewById(R.id.textViewItem)
        val textViewItemCount: TextView = itemView.findViewById(R.id.textViewItemCount)
    }

    interface OnItemClickListener {
        fun onItemClick(shopItem: ShopItem)
    }

    companion object{
        const val VIEW_TYPE_ENABLED = 1
        const val VIEW_TYPE_DISABLED = 0

        const val MAX_POOL_SIZE_VIEW_TYPE_ENABLED = 12
        const val MAX_POOL_SIZE_VIEW_TYPE_DISABLED = 8
    }
}