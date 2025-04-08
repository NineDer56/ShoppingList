package com.example.shoppinglist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.OnLongClickListener
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.data.ShopItemDatabase
import com.example.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var recyclerViewShopItems: RecyclerView
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpRecyclerView()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getShopList().observe(this) {
            shopListAdapter.shopItems = it
        }
    }

    private fun setUpRecyclerView() {
        recyclerViewShopItems = findViewById(R.id.recyclerViewShopItems)
        shopListAdapter = ShopListAdapter()

        recyclerViewShopItems.apply {
            adapter = shopListAdapter

            recycledViewPool.apply {
                setMaxRecycledViews(
                    ShopListAdapter.VIEW_TYPE_ENABLED,
                    ShopListAdapter.MAX_POOL_SIZE_VIEW_TYPE_ENABLED
                )
                setMaxRecycledViews(
                    ShopListAdapter.VIEW_TYPE_DISABLED,
                    ShopListAdapter.MAX_POOL_SIZE_VIEW_TYPE_DISABLED
                )
            }


            shopListAdapter.setOnItemClickListener(object : ShopListAdapter.OnItemClickListener {
                override fun onItemClick(shopItem: ShopItem) {
                    viewModel.changeEnableState(shopItem)
                }
            })
        }
    }
}