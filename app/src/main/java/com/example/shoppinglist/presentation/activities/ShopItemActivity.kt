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
import com.example.shoppinglist.presentation.ShopItemFragment
import com.example.shoppinglist.presentation.viewModels.ShopItemViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text

class ShopItemActivity : AppCompatActivity() {

    private var screenMode = UNKNOWN_MODE
    private var shopItemId = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.shopItemContainer))
        { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        parseIntent()

        if(savedInstanceState == null) { // Если активити не пересоздавалась
            chooseScreenMode()
        }
    }

    private fun chooseScreenMode() {
        val fragment = when (screenMode) {
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            else -> throw RuntimeException("Unknown screen mode $screenMode")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shopItemContainer, fragment)
            .commit()
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