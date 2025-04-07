package com.example.shoppinglist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.domain.ShopItem

@Dao
interface ShopItemDao {

    @Insert
    fun addShopItem(shopItem: ShopItem)

    @Delete
    fun deleteShopItem(shopItem: ShopItem)

    @Update
    fun editShopItem(shopItem: ShopItem)

    @Query("SELECT * FROM shop_items WHERE id=:shopItemId")
    fun getShopItem(shopItemId: Int) : ShopItem

    @Query("SELECT * FROM shop_items")
    fun getShopList(): List<ShopItem>
}