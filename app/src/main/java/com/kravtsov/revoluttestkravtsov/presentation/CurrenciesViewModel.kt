package com.kravtsov.revoluttestkravtsov.presentation

import android.util.Log
import com.kravtsov.revoluttestkravtsov.domain.CurrenciesService
import com.kravtsov.revoluttestkravtsov.domain.Currency
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class CurrenciesViewModel(private val currenciesService: CurrenciesService) {

    private val currenciesViewStateUpdates = BehaviorSubject.create<CurrenciesViewState>()
    private val currenciesUpdateIntents = BehaviorSubject.create<Currency>()
    private var baseCurrency = Currency("EUR", 10.0)
    private var periodicCurrenciesUpdate: Disposable? = null

    ////

    init {
        currenciesUpdateIntents.flatMapSingle { currency ->
            currenciesService.calculateCurrencies(currency).doOnError { error -> Log.d("LOG", error.message) }}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { currencies ->
                    currenciesViewStateUpdates.onNext(CurrenciesViewState(false, currencies)) }

    }

    ////

    fun getViewStateUpdates(): BehaviorSubject<CurrenciesViewState> = currenciesViewStateUpdates

    //// VIEW EVENTS

    fun onViewAppeared() {
        currenciesViewStateUpdates.onNext(CurrenciesViewState(true, null))
        periodicCurrenciesUpdate = Flowable.interval(1, TimeUnit.SECONDS)
                .subscribe { _ -> currenciesUpdateIntents.onNext(baseCurrency) }

    }

    fun onCurrencySelected(currency: Currency) {
        currenciesUpdateIntents.onNext(currency)
        baseCurrency = currency
    }

    fun onViewDisappeared() {
        periodicCurrenciesUpdate?.dispose()
    }
}
