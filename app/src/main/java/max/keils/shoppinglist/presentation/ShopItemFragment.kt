package max.keils.shoppinglist.presentation

import android.content.Context
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

class ShopItemFragment : Fragment() {

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var saveButton: Button

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view = view)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        addTextChangeListeners()
        launchRightMode()
        observeViewModels()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener)
            onEditingFinishedListener = context
        else throw RuntimeException("Activity must implement OnEditingFinishedListener")
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
             onEditingFinishedListener.onEditingFinished()
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
         val args = requireArguments()
         if (!args.containsKey(SCREEN_MODE))
             throw RuntimeException("Param screen mode is absent!")

         val mode = args.getString(SCREEN_MODE)
         if (mode != MODE_EDIT && mode != MODE_ADD)
             throw RuntimeException("Unknown screen mode: $mode")

         screenMode = mode

         if (screenMode == MODE_EDIT) {
             if (!args.containsKey(SHOP_ITEM_ID))
                 throw RuntimeException("Param shop item id is absent!")

             shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
         }
     }

     private fun initViews(view: View) {
         tilName = view.findViewById(R.id.til_name)
         tilCount = view.findViewById(R.id.til_count)
         etName = view.findViewById(R.id.et_name)
         etCount = view.findViewById(R.id.et_count)
         saveButton = view.findViewById(R.id.save_button)
     }

    interface OnEditingFinishedListener {
        fun onEditingFinished()
    }

    companion object {
        private const val SCREEN_MODE = "EXTRA_MODE"
        private const val SHOP_ITEM_ID = "EXTRA_SHOP_ITEM_ID"
        private const val MODE_EDIT = "MODE_EDIT"
        private const val MODE_ADD = "MODE_ADD"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            val args = Bundle().apply {
                putString(SCREEN_MODE, MODE_ADD)
            }
            return ShopItemFragment().apply {
                arguments = args
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            val args = Bundle().apply {
                putString(SCREEN_MODE, MODE_EDIT)
                putInt(SHOP_ITEM_ID, shopItemId)
            }
            return ShopItemFragment().apply {
                arguments = args
            }
        }
    }
}