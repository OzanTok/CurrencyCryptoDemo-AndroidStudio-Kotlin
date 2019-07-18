package com.ozantok3.currencycryptodem

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CoinMarketCapAPI {

    companion object {

        private val handler = Handler(Looper.getMainLooper())
        private val symbolList = mutableListOf<Symbol>()

        fun update(
            favoritesIdList: List<String>,
            onResult: (isSuccess: Boolean, message: String, resultList: List<Symbol>) -> Unit
        ) {
            onResult(true, "", filterFavorites(favoritesIdList))

        }

        private fun filterFavorites(favoritesIdList: List<String>): List<Symbol> {
            return symbolList.filter { favoritesIdList.contains(it.id) }
        }

        fun ticker(
            checkFavorites: Boolean,
            favoritesIdList: List<String>,
            onResult: (isSuccess: Boolean, message: String, resultList: List<Symbol>) -> Unit
        ) {
            Thread {
                val apiUrl = "https://api.coinmarketcap.com/v1/ticker/"
                var result = ""
                var exception: java.lang.Exception? = null
                try {
                    val url = URL(apiUrl)
                    val connect = url.openConnection() as HttpURLConnection
                    connect.readTimeout = 5000
                    connect.connectTimeout = 5000
                    connect.requestMethod = "GET"
                    connect.doOutput = false
                    connect.connect()
                    val responseCode: Int = connect.responseCode
                    Log.d("Call", "ResponseCode $responseCode")
                    if (responseCode == 200) {
                        val tempStream: InputStream = connect.inputStream
                        result = convertToString(tempStream)
                        if (result.isNotBlank()) {
                            val newList = convertToList(result)
                            newList.forEach { symbol ->
                                symbolList.forEach { oldSymbol ->
                                    if (symbol.id == oldSymbol.id) {
                                        when {
                                            symbol.priceUSD > oldSymbol.priceUSD -> symbol.updateDirection = 1
                                            symbol.priceUSD < oldSymbol.priceUSD -> symbol.updateDirection = -1
                                            else -> symbol.updateDirection = 0
                                        }
                                    }
                                }
                            }

                            symbolList.clear()
                            symbolList.addAll(newList)
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Call", "Error in doInBackground " + e.message)
                    e.printStackTrace()
                    exception = e
                }
                handler.post {
                    if (exception != null) {
                        onResult(false, exception.message ?: "", mutableListOf())
                    } else {
                        if (checkFavorites) {
                            onResult(true, "", filterFavorites(favoritesIdList))
                        } else {
                            onResult(true, "", symbolList)
                        }

                    }
                }
            }.start()
        }

        private fun convertToString(inStream: InputStream): String {
            var result = ""
            val isReader = InputStreamReader(inStream)
            val bReader = BufferedReader(isReader)
            var tempStr: String?
            try {
                while (true) {
                    tempStr = bReader.readLine()
                    if (tempStr == null) {
                        break
                    }
                    result += tempStr
                }
            } catch (Ex: Exception) {
                Log.e("3", "Error in ConvertToString " + Ex.printStackTrace())
            }
            return result
        }

        private fun convertToList(
            result: String
        ): List<Symbol> {
            val symbolList = mutableListOf<Symbol>()
            val jsonArray = JSONArray(result)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)

                val id = jsonObject.optString("id", "")
                val name = jsonObject.optString("name", "")
                val code = jsonObject.optString("symbol", "")
                val rank = jsonObject.optInt("rank", 0)
                val priceUSD = jsonObject.optDouble("price_usd", 0.0)
                val priceBTC = jsonObject.optDouble("price_btc", 0.0)
                val volumeUSD = jsonObject.optDouble("24h_volume_usd", 0.0)
                val marketCapUSD = jsonObject.optDouble("market_cap_usd", 0.0)
                val availableSupply = jsonObject.optDouble("available_supply", 0.0)
                val totalSupply = jsonObject.optDouble("total_supply", 0.0)
                val maxSupply = jsonObject.optDouble("max_supply", 0.0)
                val percentChange1h = jsonObject.optDouble("percent_change_1h", 0.0)
                val percentChange24h = jsonObject.optDouble("percent_change_24h", 0.0)
                val percentChange7d = jsonObject.optDouble("percent_change_7d", 0.0)
                val lastUpdate = jsonObject.optLong("last_updated", 0)

                val symbol = Symbol(
                    id = id,
                    code = code,
                    name = name,
                    rank = rank,
                    priceUSD = priceUSD,
                    priceBTC = priceBTC,
                    volumeUSD = volumeUSD,
                    marketCapUSD = marketCapUSD,
                    availableSupply = availableSupply,
                    totalSupply = totalSupply,
                    maxSupply = maxSupply,
                    percentChange1h = percentChange1h,
                    percentChange24h = percentChange24h,
                    percentChange7d = percentChange7d,
                    lastUpdate = lastUpdate
                )
                symbolList.add(symbol)
            }
            return symbolList
        }
    }
}

