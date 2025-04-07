package com.example.shoppinglist.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("shop_items")
data class ShopItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("count") val count: Int,
    @ColumnInfo("is_enable") val isEnable: Boolean
)
