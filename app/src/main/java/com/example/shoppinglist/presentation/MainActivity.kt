package com.example.shoppinglist.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
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

        setUpItemTouchHelper()

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


//            shopListAdapter.setOnShopItemLongClickListener(object :
//                ShopListAdapter.OnShopItemLongClickListener {
//                override fun onShopItemLongClick(shopItem: ShopItem) {
//                    viewModel.changeEnableState(shopItem)
//                }
//            })

            shopListAdapter.onShopItemLongClickListener = {
                viewModel.changeEnableState(it)
            }

            shopListAdapter.onShopItemClickListener = {
                TODO()
            }
        }
    }


    private fun setUpItemTouchHelper() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                viewModel.deleteShopItem(shopListAdapter.shopItems[pos])
            }

        }).also {
            it.attachToRecyclerView(recyclerViewShopItems)
        }
    }
}