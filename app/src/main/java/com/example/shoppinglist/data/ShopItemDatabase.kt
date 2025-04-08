package com.example.shoppinglist.data

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shoppinglist.domain.ShopItem

@Database(entities = [ShopItem::class], version = 1)
abstract class ShopItemDatabase : RoomDatabase() {

    abstract fun shopItemDao(): ShopItemDao

    companion object {

        private var instance: ShopItemDatabase? = null

        fun getInstance(context: Context): ShopItemDatabase {
            return instance ?: Room.databaseBuilder(
                context.applicationContext,
                ShopItemDatabase::class.java,
                "shop_items"
            ).allowMainThreadQueries()
                .build()
                .also { instance = it }
        }
    }
}