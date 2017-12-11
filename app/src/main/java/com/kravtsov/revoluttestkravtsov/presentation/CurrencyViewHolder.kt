package com.kravtsov.revoluttestkravtsov.presentation

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.kravtsov.revoluttestkravtsov.domain.Currency
import com.kravtsov.revoluttestkravtsov.R

class CurrencyViewHolder(view: View, private val clickListener: (Currency) -> Unit): RecyclerView.ViewHolder(view) {

    private val abbreviationTextView: TextView = view.findViewById(R.id.abbreviationTextView)
    private val valueEditText: EditText = view.findViewById(R.id.valueEditText)
    private val clickInterceptorView: View = view.findViewById(R.id.clickInterceptorView)
    private val valueTextChangeListener: TextWatcher
    private lateinit var currency: Currency

    ////

    init {
        valueTextChangeListener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (adapterPosition == 0) clickListener(Currency(
                        currency.abbreviation,
                        if (s?.isEmpty() != false) 0.0 else s.toString().toDouble())
                )
            }
        }

        clickInterceptorView.setOnClickListener { _ ->
            clickListener(currency)
            clickInterceptorView.visibility = View.GONE
            valueEditText.requestFocus()
        }

        valueEditText.addTextChangedListener(valueTextChangeListener)
    }

    ////

    fun bind(currency: Currency) {
        this.currency = currency
        abbreviationTextView.text = currency.abbreviation
        clickInterceptorView.visibility = View.VISIBLE

        if (adapterPosition == 0) {
            valueEditText.removeTextChangedListener(valueTextChangeListener)
            valueEditText.text = SpannableStringBuilder(String.format("%.2f", currency.value))
            valueEditText.setSelection(valueEditText.text.length)
            valueEditText.addTextChangedListener(valueTextChangeListener)
        }
        else {
            valueEditText.text = SpannableStringBuilder(String.format("%.2f", currency.value))
            valueEditText.setSelection(valueEditText.text.length)
        }
    }
}




















