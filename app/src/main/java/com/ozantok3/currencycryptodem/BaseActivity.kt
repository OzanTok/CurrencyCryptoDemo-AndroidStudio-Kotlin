package com.ozantok3.currencycryptodem

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

abstract class BaseActivity : AppCompatActivity() {



    fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private var sharedPreferences: SharedPreferences? = null
        private const val KEY_FAVORITE_IDS = "KEY_FAVORITE_IDS"

        fun getPreferences(context: Context): SharedPreferences? {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences("CC", Context.MODE_PRIVATE)
            }
            return sharedPreferences
        }

        fun isItemInFavorites(context: Context, id: String): Boolean{
            return getFavorites(context).contains(id)
        }

        fun addItemToFavorites(context: Context, id: String){
            val favorites = getFavorites(context).toMutableList()
            if(!favorites.contains(id)){
                favorites.add(id)
            }
            val text = favorites.joinToString("|")
            getPreferences(context)?.edit()?.putString(KEY_FAVORITE_IDS, text)?.apply()
        }

        fun removeItemFromFavorites(context: Context, id: String){
            val favorites = getFavorites(context).toMutableList()
            if(favorites.contains(id)){
                favorites.remove(id)
            }
            val text = favorites.joinToString("|")
            getPreferences(context)?.edit()?.putString(KEY_FAVORITE_IDS, text)?.apply()
        }

        fun getFavorites(context: Context): List<String> {
            return getPreferences(context)?.getString(KEY_FAVORITE_IDS, "")?.split("|") ?: mutableListOf()
        }
    }


}
