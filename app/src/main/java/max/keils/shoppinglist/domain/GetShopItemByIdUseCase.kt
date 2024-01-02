package max.keils.shoppinglist.domain

class GetShopItemByIdUseCase(private val repository: ShopListRepository) {

    fun getShopItemById(id: Int): ShopItem {
        return repository.getShopItemById(id = id)
    }

}