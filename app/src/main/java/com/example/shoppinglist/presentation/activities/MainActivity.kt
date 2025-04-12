package com.example.shoppinglist.presentation.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.presentation.ShopItemFragment
import com.example.shoppinglist.presentation.ShopListAdapter
import com.example.shoppinglist.presentation.viewModels.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var viewModel: MainViewModel

    private lateinit var recyclerViewShopItems: RecyclerView
    private lateinit var shopListAdapter: ShopListAdapter

    private lateinit var buttonAdd: FloatingActionButton

    private var shopItemContainer : FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
        setOnClickListeners()
        setUpRecyclerView()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getShopList().observe(this) {
            shopListAdapter.submitList(it)
        }

        setUpItemTouchHelper()

        shopItemContainer= findViewById(R.id.shopItemContainer)


    }

    override fun onEditingFinished(){
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    private fun isOnePaneMode() : Boolean = shopItemContainer == null

    private fun launchFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shopItemContainer, fragment)
            .addToBackStack(null)
            .commit()
    }


    private fun initViews() {
        buttonAdd = findViewById(R.id.buttonAddShopItem)
    }

    private fun setOnClickListeners() {
        buttonAdd.setOnClickListener {
            if(isOnePaneMode()){
                startActivity(ShopItemActivity.newIntentAddItem(this))
            } else{
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
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

            shopListAdapter.onShopItemLongClickListener = {
                viewModel.changeEnableState(it)
            }

            shopListAdapter.onShopItemClickListener = {
                if(isOnePaneMode()) {
                    startActivity(ShopItemActivity.newIntentEditItem(applicationContext, it.id))
                } else {
                    launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
                }
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
                viewModel.deleteShopItem(shopListAdapter.currentList[pos])
            }

        }).also {
            it.attachToRecyclerView(recyclerViewShopItems)
        }
    }
}