package max.keils.shoppinglist.presentation

import androidx.recyclerview.widget.DiffUtil
import max.keils.shoppinglist.domain.ShopItem

class ShopItemDiffCallback() : DiffUtil.ItemCallback<ShopItem>() {
    override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem == newItem
    }
}