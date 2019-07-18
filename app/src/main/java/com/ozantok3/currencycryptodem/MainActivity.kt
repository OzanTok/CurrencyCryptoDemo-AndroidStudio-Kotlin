package com.ozantok3.currencycryptodem

import android.os.Bundle
import android.widget.ImageView

class MainActivity : BaseSymbolListActivity() {

    override fun getToolbarTitle(): String {
        return "Crypto Currencies"
    }

    override fun shouldCheckFavorites(): Boolean {
        return false
    }
}

