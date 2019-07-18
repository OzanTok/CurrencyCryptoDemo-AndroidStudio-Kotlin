package com.ozantok3.currencycryptodem

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Symbol (
    val id: String = "",
    val name: String = "",
    val code: String = "",
    val rank: Int = 0,
    val priceUSD: Double = 0.0,
    val priceBTC: Double = 0.0,
    val volumeUSD: Double = 0.0,
    val marketCapUSD: Double = 0.0,
    val availableSupply: Double = 0.0,
    val totalSupply: Double = 0.0,
    val maxSupply: Double = 0.0,
    val percentChange1h: Double = 0.0,
    val percentChange24h: Double = 0.0,
    val percentChange7d: Double = 0.0,
    val lastUpdate: Long = 0,
    var updateDirection: Int = 0 /* -1 , 0 , 1 */
): Parcelable