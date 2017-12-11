package com.kravtsov.revoluttestkravtsov.presentation

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.kravtsov.revoluttestkravtsov.domain.Currency
import com.kravtsov.revoluttestkravtsov.R

class CurrenciesRecyclerAdapter(private val recyclerView: RecyclerView,
                                private val clickListener: (Currency) -> Unit): RecyclerView.Adapter<CurrencyViewHolder>() {

    private val currencies = mutableListOf<Currency>()

    ////

    fun update(currencies: List<Currency>) {
        when {
            this.currencies.isNotEmpty() && this.currencies[0] != currencies[0] -> {
                val positionToMoveFrom = this.currencies.indexOf(currencies[0])
                this.currencies.add(0, this.currencies.removeAt(positionToMoveFrom))
                notifyItemMoved(positionToMoveFrom, 0)
                recyclerView.scrollToPosition(0)
            }
            this.currencies.isNotEmpty() -> {
                for (i in 1 until this.currencies.size) {
                    for (currency in currencies) {
                        if (this.currencies[i] == currency) {
                            this.currencies[i] = currency
                            notifyItemChanged(i)
                        }
                    }
                }
            }
            else -> {
                this.currencies.addAll(currencies)
                notifyDataSetChanged()
            }
        }
    }

    //// RECYCLER ADAPTER

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = CurrencyViewHolder(LayoutInflater.from(parent?.context)
            .inflate(R.layout.list_item_currency, parent, false), clickListener)

    override fun onBindViewHolder(holder: CurrencyViewHolder?, position: Int) {
        holder?.bind(currencies[position])
    }

    override fun getItemCount() = currencies.size
}