package com.kravtsov.revoluttestkravtsov.presentation

import com.kravtsov.revoluttestkravtsov.domain.Currency

data class CurrenciesViewState(val loading: Boolean,
                               var currencies: List<Currency>?)