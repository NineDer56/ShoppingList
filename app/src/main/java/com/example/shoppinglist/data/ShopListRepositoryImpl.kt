package com.example.shoppinglist.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository

class ShopListRepositoryImpl(context: Context) : ShopListRepository {

    private var shopItemDao : ShopItemDao = ShopItemDatabase.getInstance(context).shopItemDao()

    override fun addShopItem(shopItem: ShopItem) {
        shopItemDao.addShopItem(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopItemDao.deleteShopItem(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        shopItemDao.editShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopItemDao.getShopItem(shopItemId)
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopItemDao.getShopList()
    }
}