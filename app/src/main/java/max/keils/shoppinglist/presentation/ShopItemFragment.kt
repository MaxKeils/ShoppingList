package max.keils.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import max.keils.shoppinglist.R
import max.keils.shoppinglist.domain.ShopItem

class ShopItemFragment(
    private val screenMode: String = MODE_UNKNOWN,
    private val shopItemId: Int = ShopItem.UNDEFINED_ID
) : Fragment() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var saveButton: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parseParams()
        initViews(view = view)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        addTextChangeListeners()
        launchRightMode()
        observeViewModels()
    }

     private fun observeViewModels() {
         viewModel.errorInputName.observe(viewLifecycleOwner) {
             if (it)
                 tilName.error = "Incorrect input name!"
             else
                 tilName.error = null
         }
         viewModel.errorInputCount.observe(viewLifecycleOwner) {
             if (it)
                 tilCount.error = "Incorrect input count"
             else
                 tilCount.error = null
         }
         viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
             activity?.onBackPressed()
         }
     }

     private fun launchRightMode() {
         when (screenMode) {
             MODE_EDIT -> launchEditMode()
             MODE_ADD -> launchAddMode()
         }
     }

     private fun addTextChangeListeners() {
         etName.addTextChangedListener(object : TextWatcher {
             override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

             }

             override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                 viewModel.resetErrorInputName()
             }

             override fun afterTextChanged(p0: Editable?) {

             }

         })
         etCount.addTextChangedListener(object : TextWatcher {
             override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

             }

             override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                 viewModel.resetErrorInputCount()
             }

             override fun afterTextChanged(p0: Editable?) {
             }

         })
     }

     private fun launchEditMode() {
         viewModel.getShopItemById(shopItemId)
         viewModel.shopItem.observe(viewLifecycleOwner) {
             etName.setText(it.name)
             etCount.setText(it.count.toString())
         }

         saveButton.setOnClickListener {
             viewModel.editShopItem(etName.text.toString(), etCount.text.toString())
         }
     }

     private fun launchAddMode() {
         saveButton.setOnClickListener {
             viewModel.addShopItem(etName.text.toString(), etCount.text.toString())
         }
     }

     private fun parseParams() {
         if (screenMode != MODE_ADD && screenMode != MODE_EDIT)
             throw RuntimeException("Param screen mode is absent!")
         if (screenMode == MODE_EDIT && shopItemId == ShopItem.UNDEFINED_ID)
             throw RuntimeException("Param shop item id is absent!")
     }

     private fun initViews(view: View) {
         tilName = view.findViewById(R.id.til_name)
         tilCount = view.findViewById(R.id.til_count)
         etName = view.findViewById(R.id.et_name)
         etCount = view.findViewById(R.id.et_count)
         saveButton = view.findViewById(R.id.save_button)
     }

    companion object {
        private const val MODE_EDIT = "MODE_EDIT"
        private const val MODE_ADD = "MODE_ADD"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment(screenMode = MODE_ADD)
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment(screenMode = MODE_EDIT, shopItemId = shopItemId)
        }

    }


}