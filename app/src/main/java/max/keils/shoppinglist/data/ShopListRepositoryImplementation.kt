package max.keils.shoppinglist.data

import max.keils.shoppinglist.domain.ShopItem
import max.keils.shoppinglist.domain.ShopListRepository
import java.lang.RuntimeException

object ShopListRepositoryImplementation: ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0

    init {
        for (i in 0 until 10) {
            val item = ShopItem("name $i", i, true)
            addShopItem(item = item)
        }
    }

    override fun addShopItem(item: ShopItem) {
        if (item.id == ShopItem.UNDEFINED_ID)
            item.id = autoIncrementId++
        shopList.add(item)
    }

    override fun deleteShopItem(item: ShopItem) {
        shopList.remove(item)
    }

    override fun editShopItem(item: ShopItem) {
        val oldItem = getShopItemById(item.id)
        deleteShopItem(oldItem)
        addShopItem(item)
    }

    override fun getShopItemById(id: Int): ShopItem {
        return shopList.find {
            it.id == id
        } ?:
            throw RuntimeException("Element with id $id not found")
    }

    override fun getShopList(): List<ShopItem> {
        return shopList.toList()
    }

}