package com.example.shoppinglist.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.shoppinglist.domain.ShopItem

class ShopListDiffCallback(
    private val oldList: List<ShopItem>,
    private val newList: List<ShopItem>
): DiffUtil.Callback() {
    override fun getOldListSize()= oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }
    // если areItemsTheSame вернул true, то вызывается метод areContentsTheSame
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
        // метод equals переопределен у data классов

//        return (oldItem.name == newItem.name &&
//                oldItem.count == newItem.count &&
//                oldItem.isEnable == newItem.isEnable)
    }
}

// Допустим мы переводим один объект в состояиние isEnable = false
// areItemsTheSame сравнит id старого и нового объекта, увидит, что они совпадают
// и поймет, что это один и тот же объект и вернет true

// areContentsTheSame сравнит все остальные поля, увидит, что изменилось поле isEnable
// и перерисует объект