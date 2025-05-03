package com.example.shoppinglist.data

import com.example.shoppinglist.domain.ShopItem

class ShopItemMapper {

    fun mapEntityToDbModel(shopItem: ShopItem) : ShopItemDbModel{
        return ShopItemDbModel(
            id = shopItem.id,
            name = shopItem.name,
            count = shopItem.count,
            isEnable = shopItem.isEnable
        )
    }

    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel) : ShopItem{
        return ShopItem(
            id = shopItemDbModel.id,
            name = shopItemDbModel.name,
            count = shopItemDbModel.count,
            isEnable = shopItemDbModel.isEnable
        )
    }

    fun mapListDbModelToListEntity(shopItemDbModel : List<ShopItemDbModel>) : List<ShopItem>{
        return shopItemDbModel.map { mapDbModelToEntity(it) }
    }
}