package com.ozantok3.currencycryptodem

import android.R.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.text.DecimalFormat
import java.text.NumberFormat

import java.util.*


class SymbolItemAdapter(
    private val onClickCallback: (Symbol) -> Unit,
    private val onLongClickCallback: (Symbol) -> Unit
) : RecyclerView.Adapter<SymbolItemViewHolder>() {


    private val items = mutableListOf<Symbol>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymbolItemViewHolder {
        return SymbolItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_symbol_item, parent, false)
        )

    }

    override fun getItemCount() = items.size


    override fun onBindViewHolder(holder: SymbolItemViewHolder, position: Int) {
        holder.bindItem(items[position], onClickCallback, onLongClickCallback)
    }

    fun updateItems(items: List<Symbol>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}

class SymbolItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val textViewCode: TextView by lazy { itemView.findViewById<TextView>(R.id.rowSymbolItem_textView_code) }
    private val textViewName: TextView by lazy { itemView.findViewById<TextView>(R.id.rowSymbolItem_textView_name) }
    private val textViewPriceUsd: TextView by lazy { itemView.findViewById<TextView>(R.id.rowSymbolItem_textView_priceInUsd) }
    private val textViewPriceBtc: TextView by lazy { itemView.findViewById<TextView>(R.id.rowSymbolItem_textView_priceInBtc) }
    private val textViewPercentageChange: TextView by lazy { itemView.findViewById<TextView>(R.id.rowSymbolItem_textView_percentageChange) }
    private val textViewLastUpdate: TextView by lazy { itemView.findViewById<TextView>(R.id.rowSymbolItem_textView_lastUpdateTime) }
    private val imageViewChange: ImageView by lazy { itemView.findViewById<ImageView>(R.id.rowSymbolItem_imageView_change) }



    fun Long.dateFormatter(): String {
        val sdf = java.text.SimpleDateFormat(" HH:mm:ss")
        val date = java.util.Date()
        return sdf.format(date)
    }

    fun Double.decimalFormatter(): String {
        val pattern = "###,###.###"
        val decimalFormat = DecimalFormat(pattern)
        return decimalFormat.format(this)
    }

    fun bindItem(symbol: Symbol, onClickCallback: (Symbol) -> Unit, onLongClickCallback: (Symbol) -> Unit) {

        var local: Locale = Locale("en", "IN")
        var format: NumberFormat = NumberFormat.getCurrencyInstance(local)

        textViewCode.text = symbol.code
        textViewLastUpdate.text = symbol.lastUpdate.dateFormatter()
        textViewName.text = symbol.name
        textViewPercentageChange.text = "% " + symbol.percentChange24h.toString()
        textViewPriceBtc.text = symbol.priceBTC.decimalFormatter() + "฿"
        textViewPriceUsd.text = "$" + symbol.priceUSD.decimalFormatter()
        itemView.setOnClickListener { onClickCallback(symbol) }
        itemView.setOnLongClickListener {
            onLongClickCallback(symbol)
            true
        }

        if (symbol.percentChange24h.toString().startsWith("-")) {
            textViewPercentageChange.setTextColor(ContextCompat.getColor(itemView.context, color.holo_red_dark))
        } else {
            textViewPercentageChange.setTextColor(ContextCompat.getColor(itemView.context, color.holo_green_dark))
        }

        when {
            symbol.updateDirection == 1 -> imageViewChange.setImageResource(R.drawable.up24)
            symbol.updateDirection == -1 -> imageViewChange.setImageResource(R.drawable.down24)
            else -> imageViewChange.setImageResource(R.drawable.fix24)
        }

        when {
            symbol.updateDirection == 1 -> transitionDrawable(itemView)
            symbol.updateDirection == -1 -> transitionDrawable(itemView)
        }
    }

    fun transitionDrawable(view: View) {
        val color = arrayOf(ColorDrawable(Color.BLACK), ColorDrawable(Color.LTGRAY))
        val trans = TransitionDrawable(color)
        if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(trans)
        } else {
            view.background = trans
        }
        trans.startTransition(300)
        trans.reverseTransition(300)
    }

}

