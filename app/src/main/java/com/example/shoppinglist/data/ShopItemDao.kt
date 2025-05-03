package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.domain.ShopItem

@Dao
interface ShopItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addShopItem(shopItem: ShopItemDbModel)

    @Delete
    fun deleteShopItem(shopItem: ShopItemDbModel)

    @Update
    fun editShopItem(shopItem: ShopItemDbModel)

    @Query("SELECT * FROM shop_items WHERE id=:id")
    fun getShopItem(id: Int) : ShopItemDbModel

    @Query("SELECT * FROM shop_items")
    fun getShopList(): LiveData<List<ShopItemDbModel>>
}