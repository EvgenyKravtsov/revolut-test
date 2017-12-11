package com.kravtsov.revoluttestkravtsov.domain

import com.kravtsov.revoluttestkravtsov.domain.Currency
import io.reactivex.Single

interface CurrenciesService {

    fun calculateCurrencies(currency: Currency): Single<List<Currency>>
}