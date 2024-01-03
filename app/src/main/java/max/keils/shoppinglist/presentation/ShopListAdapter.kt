package max.keils.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import max.keils.shoppinglist.R
import max.keils.shoppinglist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopItemViewHolder>() {

    val shopList = listOf<ShopItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val view = LayoutInflater.from(parent.context).
            inflate(R.layout.item_shop_disabled, parent, false)
        return ShopItemViewHolder(view = view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count.toString()
        holder.itemView.setOnLongClickListener {
            true
        }
    }

    override fun getItemCount(): Int {
        return shopList.size
    }
}