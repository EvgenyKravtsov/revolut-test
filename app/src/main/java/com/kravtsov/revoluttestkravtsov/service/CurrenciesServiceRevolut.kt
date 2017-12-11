package com.kravtsov.revoluttestkravtsov.service

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.kravtsov.revoluttestkravtsov.domain.CurrenciesService
import com.kravtsov.revoluttestkravtsov.domain.Currency
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class CurrenciesServiceRevolut: CurrenciesService {

    companion object {
        const val REVOLUT_API_BASE_URL = "https://revolut.duckdns.org"
    }

    ////

    private val revolutApi: RevolutApi

    ////

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(REVOLUT_API_BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        revolutApi = retrofit.create(RevolutApi::class.java)
    }

    //// CURRENCIES SERVICE

    override fun calculateCurrencies(currency: Currency): Single<List<Currency>> {
        return Single.create { subscription: SingleEmitter<List<Currency>> ->
            val currencyResponse = revolutApi.currenciesRates(currency.abbreviation).execute().body() ?:
                    throw Exception("Could not fetch currency rates from Revolut remote service!!! ")
            val calculatedCurrencies = currencyResponse.rates.keySet().map { key ->
                Currency(key, currencyResponse.rates.get(key).asDouble * currency.value)
            }
                    .toMutableList()
            calculatedCurrencies.add(0, currency)
            subscription.onSuccess(calculatedCurrencies)
        }.subscribeOn(Schedulers.io())
    }
}

interface RevolutApi {

    @GET("latest")
    fun currenciesRates(@Query("base") baseCurrencyAbbreviation: String): Call<CurrencyResponse>
}

data class CurrencyResponse(@SerializedName("base") val base: String,
                            @SerializedName("date") val date: String,
                            @SerializedName("rates") val rates: JsonObject)









































