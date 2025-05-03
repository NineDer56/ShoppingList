package com.example.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository

class ShopListRepositoryImpl(application: Application) : ShopListRepository {

    private val shopItemDao : ShopItemDao = ShopItemDatabase.getInstance(application).shopItemDao()
    private val mapper = ShopItemMapper()

    override suspend fun addShopItem(shopItem: ShopItem) {
        shopItemDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopItemDao.deleteShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        shopItemDao.editShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem {
        val dbModel = shopItemDao.getShopItem(shopItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopItemDao.getShopList().map {
            mapper.mapListDbModelToListEntity(it)
        }
    }

//    override fun getShopList(): LiveData<List<ShopItem>> {
//        val dbModelList : LiveData<List<ShopItemDbModel>> =  shopItemDao.getShopList()
//
//        return MediatorLiveData<List<ShopItem>>().apply {
//            addSource(dbModelList) {
//                mapper.mapListDbModelToListEntity(it)
//            }
//        }
//    }
}