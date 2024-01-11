package max.keils.shoppinglist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import max.keils.shoppinglist.data.ShopListRepositoryImplementation
import max.keils.shoppinglist.domain.DeleteShopItemUseCase
import max.keils.shoppinglist.domain.EditShopItemUseCase
import max.keils.shoppinglist.domain.GetShopListUseCase
import max.keils.shoppinglist.domain.ShopItem

class MainViewModel: ViewModel() {

    private val repository = ShopListRepositoryImplementation

    private val getShopListUseCase = GetShopListUseCase(repository = repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository = repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository = repository)

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(item: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(item = item)
    }

    fun changeEnabledState(item: ShopItem) {
        val newItem = item.copy(enabled = !item.enabled)
        editShopItemUseCase.editShopItem(newItem)
    }

}