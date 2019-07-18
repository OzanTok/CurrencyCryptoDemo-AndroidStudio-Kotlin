package com.ozantok3.currencycryptodem

class FavoritesActivity: BaseSymbolListActivity() {

    override fun getToolbarTitle(): String {
        return "Favorites"
    }

    override fun shouldCheckFavorites(): Boolean {
        return true
    }
}