package com.ozantok3.currencycryptodem

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.row_symbol_item.*
import java.text.DecimalFormat

class DetailActivity : BaseActivity() {

    private val symbol: Symbol? by lazy { intent?.extras?.getParcelable<Symbol>(EXTRAS_SYMBOL) }

    fun Long.dateFormatter(): String {
        val simpleDateFormat = java.text.SimpleDateFormat("HH:mm:ss")
        val date = java.util.Date()
        return simpleDateFormat.format(date)
    }

    fun Double.decimalFormatter(): String {
        val pattern = "###,###.##############"
        val decimalFormat = DecimalFormat(pattern)
        return decimalFormat.format(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        Log.i("DetailActivity", "Symbol $symbol")
        activityDetail_textView_ID.text = symbol?.id
        activityDetail_textView_name.text = symbol?.name
        activityDetail_textView_symbol.text = symbol?.code
        activityDetail_textView_rank.text = symbol?.rank.toString()
        activityDetail_textView_priceUSD.text = symbol?.priceUSD?.decimalFormatter()
        activityDetail_textView_priceBTC.text = symbol?.priceBTC?.decimalFormatter()
        activityDetail_textView_24hVolumeUSD.text = symbol?.volumeUSD?.decimalFormatter()
        activityDetail_textView_marketCapUSD.text = symbol?.marketCapUSD?.decimalFormatter()
        activityDetail_textView_availableSupply.text = symbol?.availableSupply?.decimalFormatter()
        activityDetail_textView_totalSupply.text = symbol?.totalSupply?.decimalFormatter()
        activityDetail_textView_maxSupply.text = symbol?.maxSupply?.decimalFormatter()
        activityDetail_textView_percentChange1h.text = symbol?.percentChange1h.toString()
        activityDetail_textView_percentChange24h.text = symbol?.percentChange24h?.decimalFormatter()
        activityDetail_textView_percentChange7d.text = symbol?.percentChange7d?.decimalFormatter()
        activityDetail_textView_lastUpdate.text = symbol?.lastUpdate?.dateFormatter()
        activityDetails_imageView_cryptoImage.setImageDrawable(imageResource(activityDetail_textView_symbol.text.toString()))
        if (symbol?.percentChange24h.toString().startsWith("-")) {
            activityDetail_textView_percentChange24h.setTextColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.holo_red_light
                )
            )

        } else {
            activityDetail_textView_percentChange24h.setTextColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.holo_green_dark
                )
            )

        }
        if (symbol?.percentChange1h.toString().startsWith("-")) {
            activityDetail_textView_percentChange1h.setTextColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.holo_red_light
                )
            )
        } else {
            activityDetail_textView_percentChange1h.setTextColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.holo_green_dark
                )
            )
        }
        if (symbol?.percentChange7d.toString().startsWith("-")) {
            activityDetail_textView_percentChange7d.setTextColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.holo_red_light
                )
            )
        } else {
            activityDetail_textView_percentChange7d.setTextColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.holo_green_dark
                )
            )
        }

      /*  fun imageResource(symbol: String): Drawable {
            val sym = symbol.toLowerCase()
            val uri = "@drawable/icon_$sym"
            val imgResource = this.resources.getIdentifier(uri, null, this.packageName)

            try {
                val res = this.resources.getDrawable(imgResource, null)
                return res
            } catch (e: Exception) {
                val uri2 = "@drawable/icon_"
                val imgResource2 = this.resources.getIdentifier(uri2, null, this.packageName)
                return this.resources.getDrawable(imgResource2, null)
            }
        }*/
    }


    private fun imageResource(symbol: String): Drawable {
        val sym = symbol.toLowerCase()
        val uri = "@drawable/icon_$sym"
        val imgResource = this.resources.getIdentifier(uri, null, this.packageName)

        try {
            val res = this.resources.getDrawable(imgResource, null)
            return res
        } catch (e: Exception) {
            val uri2 = "@drawable/icon_"
            val imgResource2 = this.resources.getIdentifier(uri2, null, this.packageName)
            return this.resources.getDrawable(imgResource2, null)
        }
    }

    companion object {
        const val EXTRAS_SYMBOL = "EXTRAS_SYMBOL"
    }
}