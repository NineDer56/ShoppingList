package com.example.shoppinglist.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShopItemDbModel::class], version = 1)
abstract class ShopItemDatabase : RoomDatabase() {

    abstract fun shopItemDao(): ShopItemDao

    companion object {
        private var INSTANCE: ShopItemDatabase? = null
        private val LOCK = Any()

        fun getInstance(application: Application): ShopItemDatabase {
            INSTANCE?.let {
                return it
            }

            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }

                val db = Room.databaseBuilder(
                    application,
                    ShopItemDatabase::class.java,
                    "shop_items.db"
                ).allowMainThreadQueries()
                    .build()

                INSTANCE = db
                return db
            }
        }
    }
}