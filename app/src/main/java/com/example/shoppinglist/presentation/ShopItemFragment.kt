package com.example.shoppinglist.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.presentation.viewModels.ShopItemViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment : Fragment() {

    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var editTextName: TextInputEditText
    private lateinit var textInputLayoutCount: TextInputLayout
    private lateinit var editTextCount: TextInputEditText
    private lateinit var buttonSave: Button

    private lateinit var viewModel: ShopItemViewModel

    private var screenMode: String = UNKNOWN_MODE
    private var shopItemId: Int = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shop_item, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        chooseScreenMode()
    }

    private fun chooseScreenMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)

        viewModel.shopItem.observe(this) {
            editTextName.setText(it.name)
            editTextCount.setText(it.count.toString())
        }

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val count = editTextCount.text.toString()
            viewModel.editShopItem(name, count)
        }

        observeShouldCloseScreen()
        observeErrors()
        addTextChangedListeners()
    }

    private fun launchAddMode() {

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val count = editTextCount.text.toString()
            viewModel.addShopItem(name, count)
        }

        observeShouldCloseScreen()
        observeErrors()
        addTextChangedListeners()
    }

    private fun observeShouldCloseScreen() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun observeErrors() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            textInputLayoutName.error = if (it) {
                "Incorrect name"
            } else {
                null
            }

        }

        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            textInputLayoutCount.error = if (it) {
                "Incorrect count"
            } else {
                null
            }
        }
    }

    private fun addTextChangedListeners() {
        editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        editTextCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode: $mode")
        }

        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(ITEM_ID)) {
                throw RuntimeException("Param shop id is absent")
            }
            shopItemId = args.getInt(ITEM_ID, UNDEFINED_ID)
        }

    }

    private fun initViews(view: View) {
        textInputLayoutName = view.findViewById(R.id.textInputLayoutName)
        editTextName = view.findViewById(R.id.editTextName)
        textInputLayoutCount = view.findViewById(R.id.textInputLayoutCount)
        editTextCount = view.findViewById(R.id.editTextCount)
        buttonSave = view.findViewById(R.id.buttonSave)
    }


    companion object {

        private const val ITEM_ID = "extra_id"
        private const val SCREEN_MODE = "extra_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"

        private const val UNKNOWN_MODE = "unknown_mode"
        private const val UNDEFINED_ID = -1

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(ITEM_ID, shopItemId)
                }
            }
        }
    }
}