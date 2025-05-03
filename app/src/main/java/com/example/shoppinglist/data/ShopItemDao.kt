package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ShopItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShopItem(shopItem: ShopItemDbModel)

    @Delete
    suspend fun deleteShopItem(shopItem: ShopItemDbModel)

    @Update
    suspend fun editShopItem(shopItem: ShopItemDbModel)

    @Query("SELECT * FROM shop_items WHERE id=:id")
    suspend fun getShopItem(id: Int) : ShopItemDbModel

    @Query("SELECT * FROM shop_items")
    fun getShopList(): LiveData<List<ShopItemDbModel>>
}