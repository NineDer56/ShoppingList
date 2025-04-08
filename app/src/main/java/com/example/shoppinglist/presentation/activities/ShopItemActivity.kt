package com.example.shoppinglist.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.presentation.viewModels.ShopItemViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text

class ShopItemActivity : AppCompatActivity() {

    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var editTextName: TextInputEditText
    private lateinit var textInputLayoutCount: TextInputLayout
    private lateinit var editTextCount: TextInputEditText
    private lateinit var buttonSave: Button

    private lateinit var viewModel: ShopItemViewModel

    private var screenMode = UNKNOWN_MODE
    private var shopItemId = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main))
        { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        parseIntent()
        initViews()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        chooseScreenMode()
    }

    private fun chooseScreenMode(){
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

    private fun observeShouldCloseScreen(){
        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }
    }

    private fun observeErrors(){
        viewModel.errorInputName.observe(this) {
            textInputLayoutName.error = if (it) {
                "Incorrect name"
            } else {
                null
            }

        }

        viewModel.errorInputCount.observe(this) {
            textInputLayoutCount.error = if(it) {
                "Incorrect count"
            } else {
                null
            }
        }
    }

    private fun addTextChangedListeners(){
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

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_MODE)) {
            throw RuntimeException("Screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode: $mode")
        }

        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_ID)) {
                throw RuntimeException("Param shop id is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_ID, UNDEFINED_ID)
        }

    }

    private fun initViews() {
        textInputLayoutName = findViewById(R.id.textInputLayoutName)
        editTextName = findViewById(R.id.editTextName)
        textInputLayoutCount = findViewById(R.id.textInputLayoutCount)
        editTextCount = findViewById(R.id.editTextCount)
        buttonSave = findViewById(R.id.buttonSave)
    }


    companion object {

        private const val EXTRA_ID = "extra_id"
        private const val EXTRA_MODE = "extra_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"

        private const val UNKNOWN_MODE = "unknown_mode"
        private const val UNDEFINED_ID = -1

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_ID, shopItemId)
            return intent
        }

    }
}