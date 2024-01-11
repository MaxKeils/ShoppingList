package max.keils.shoppinglist.presentation

import androidx.recyclerview.widget.DiffUtil
import max.keils.shoppinglist.domain.ShopItem

class ShopListDiffCallback(
    private val oldList: List<ShopItem>,
    private val newList: List<ShopItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        /*
        * Проверка на совпадение элементов, то есть смотрим на ID
        * */
        val oldId = oldList[oldItemPosition].id
        val newId = newList[newItemPosition].id
        return oldId == newId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        /*
        Проверяет изменились ли поля элемента, еслм хотя бы 1 поле отличается
        вернуть false
         */
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}