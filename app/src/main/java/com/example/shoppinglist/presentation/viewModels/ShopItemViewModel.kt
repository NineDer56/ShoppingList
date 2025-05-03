package com.example.shoppinglist.presentation.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.AddShopItemUseCase
import com.example.shoppinglist.domain.EditShopItemUseCase
import com.example.shoppinglist.domain.GetShopItemUseCase
import com.example.shoppinglist.domain.ShopItem
import kotlinx.coroutines.launch


class ShopItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)

    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val getShopItemUseCase = GetShopItemUseCase(repository)

    private var _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private var _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private var _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private var _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)

        if (validateNameAndCount(name, count)) {
            viewModelScope.launch {
                val shopItem = ShopItem(name = name, count = count, isEnable = true)
                addShopItemUseCase.addShopItem(shopItem)
                _shouldCloseScreen.value = Unit
            }
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)

        if (validateNameAndCount(name, count)) {
            val shopItem = _shopItem.value?.let {
                viewModelScope.launch{
                    val item = it.copy(name = name, count = count)
                    editShopItemUseCase.editShopItem(item)
                    _shouldCloseScreen.value = Unit
                }
            }

        }
    }

    fun getShopItem(id: Int) {
        viewModelScope.launch {
            val item = getShopItemUseCase.getShopItem(id)
            _shopItem.value = item
        }
    }

    private fun parseName(input: String?) = input?.trim() ?: ""

    private fun parseCount(input: String?) = input?.trim()?.toIntOrNull() ?: 0

    private fun validateNameAndCount(name: String, count: Int): Boolean {
        val nameIsValid = name.isNotBlank().also {
            if (!it) _errorInputName.value = true
        }

        val countIsValid = (count > 0).also {
            if (!it) _errorInputCount.value = true
        }

        return nameIsValid && countIsValid
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }
}