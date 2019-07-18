package com.ozantok3.currencycryptodem

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

abstract class BaseSymbolListActivity : BaseActivity() {

    private val textViewTitle: TextView by lazy { findViewById<TextView>(R.id.activityBaseSymbolList_textView_title) }
    private val recyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.activityBaseSymbolList_recyclerView) }
    private val imageViewFavorites: ImageView by lazy { findViewById<ImageView>(R.id.activityBaseSymbolList_imageView_favorites) }
    private val handler = Handler(Looper.getMainLooper())

    private val runRequestRunnable: Runnable = Runnable {
        CoinMarketCapAPI.ticker(shouldCheckFavorites(), getFavorites(this)) { isSuccess, message, resultList ->
            if (isSuccess) {
                adapter.updateItems(resultList)

            } else {
                showToast(message)
            }
            requestDelayed()
        }
    }

    private val adapter = SymbolItemAdapter({ symbol ->
        // open detail activity
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRAS_SYMBOL, symbol)
        startActivity(intent)
    }, { symbol ->
        // open add/remove favorites
        if (isItemInFavorites(this, symbol.id)) {
            AlertDialog.Builder(this)
                .setTitle(symbol.code)
                .setMessage(symbol.name)
                .setPositiveButton("Remove from Favorites") { _, _ ->
                    removeItemFromFavorites(this, symbol.id)
                    updateItems()
                }
                .setNegativeButton("Cancel") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .show()
        } else {
            AlertDialog.Builder(this)
                .setTitle(symbol.code)
                .setMessage(symbol.name)
                .setPositiveButton("Add to Favorites") { _, _ ->
                    addItemToFavorites(this, symbol.id)
                    updateItems()
                }
                .setNegativeButton("Cancel") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .show()
        }

    })

    abstract fun shouldCheckFavorites(): Boolean
    abstract fun getToolbarTitle(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_base_symbol_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        textViewTitle.text = getToolbarTitle()
        imageViewFavorites.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
        if (shouldCheckFavorites()) {
            imageViewFavorites.visibility = View.GONE
        }

    }

    override fun onStart() {
        super.onStart()
        handler.post(runRequestRunnable)
    }

    private fun requestDelayed() {
        handler.postDelayed(runRequestRunnable, 8000)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runRequestRunnable)
    }

    private fun updateItems() {
        if (shouldCheckFavorites()) {
            CoinMarketCapAPI.update(getFavorites(this)) { _, _, resultList ->
                adapter.updateItems(resultList)
            }
        }
    }
}

