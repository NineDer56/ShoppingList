package com.example.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("shop_items")
data class ShopItemDbModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("count") val count: Int,
    @ColumnInfo("is_enable") val isEnable: Boolean
)
