package com.kravtsov.revoluttestkravtsov.presentation

import com.kravtsov.revoluttestkravtsov.service.CurrenciesServiceRevolut

object ViewModelFactory {

    private var currenciesViewModel: CurrenciesViewModel? = null

    ////

    fun provideCurrenciesViewModel(): CurrenciesViewModel {
        if (currenciesViewModel == null) currenciesViewModel = CurrenciesViewModel(CurrenciesServiceRevolut())
        return currenciesViewModel!!
    }
}