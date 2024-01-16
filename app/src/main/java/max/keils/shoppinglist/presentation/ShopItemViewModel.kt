package max.keils.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import max.keils.shoppinglist.data.ShopListRepositoryImplementation
import max.keils.shoppinglist.domain.AddShopItemUseCase
import max.keils.shoppinglist.domain.EditShopItemUseCase
import max.keils.shoppinglist.domain.GetShopItemByIdUseCase
import max.keils.shoppinglist.domain.ShopItem

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImplementation

    private val addShopItemUseCase = AddShopItemUseCase(repository = repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository = repository)
    private val getShopItemByIdUseCase = GetShopItemByIdUseCase(repository = repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName : LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount : LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem : LiveData<ShopItem>
        get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen : LiveData<Unit>
        get() = _shouldCloseScreen

    fun getShopItem(id : Int) {
        val item = getShopItemByIdUseCase.getShopItemById(id)
        _shopItem.value = item
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName = inputName)
        val count = parseCount(inputCount = inputCount)

        val isFieldsValid = validateInput(name = name, count = count)
        if (isFieldsValid) {
            val shopItem = ShopItem(name = name, count = count, enabled = true)
            addShopItemUseCase.addShopItem(item = shopItem)
            finishWork()
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName = inputName)
        val count = parseCount(inputCount = inputCount)

        val isFieldsValid = validateInput(name = name, count = count)
        if (isFieldsValid) {
             _shopItem.value?.let {
                 val item = it.copy(name = name, count = count)
                 editShopItemUseCase.editShopItem(item = item)
                 finishWork()
             }
        }
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    private fun parseName(inputName : String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?) : Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int) : Boolean {
        var result = true
        if (name.isBlank())
            _errorInputName.value = true
            result = false
        if (count <= 0)
            _errorInputName.value = true
            result = false
        return result
    }



}