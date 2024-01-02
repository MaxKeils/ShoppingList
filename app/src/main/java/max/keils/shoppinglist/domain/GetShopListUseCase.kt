package max.keils.shoppinglist.domain

class GetShopListUseCase(private val repository: ShopListRepository) {

    fun getShopList(): List<ShopItem> {
        return repository.getShopList()
    }

}