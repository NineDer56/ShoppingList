package com.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

//    private var _shopItems: List<ShopItem> = mutableListOf()
//    var shopItems: List<ShopItem>
//        get() = _shopItems.toList()
//        set(value) {
//            val callback = ShopListDiffCallback(_shopItems, value)
//            val diffResult = DiffUtil.calculateDiff(callback)
//            diffResult.dispatchUpdatesTo(this)
//
//            _shopItems = value
//        }

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null

    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    private var count = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d("ShopListAdapter", "onCreateViewHolder ${++count}")
        val layoutId = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type")
        }
        val view = LayoutInflater.from(parent.context).inflate(
            layoutId, parent, false
        )
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
//        val item = _shopItems[position]
        val item = getItem(position)

        holder.apply {
            textViewItem.text = item.name
            textViewItemCount.text = item.count.toString()

            itemView.setOnLongClickListener {
                onShopItemLongClickListener?.invoke(item)
                true
            }

            itemView.setOnClickListener {
                onShopItemClickListener?.invoke(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.isEnable) VIEW_TYPE_ENABLED else VIEW_TYPE_DISABLED
    }

//    override fun getItemCount() = _shopItems.size

//    Устанавливаем значения по умолчанию
//    override fun onViewRecycled(holder: ShopListViewHolder) {
//        super.onViewRecycled(holder)
//        holder.apply {
//            textViewItem.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
//        }
//    }

    companion object {
        const val VIEW_TYPE_ENABLED = 1
        const val VIEW_TYPE_DISABLED = 0

        const val MAX_POOL_SIZE_VIEW_TYPE_ENABLED = 12
        const val MAX_POOL_SIZE_VIEW_TYPE_DISABLED = 8
    }
}