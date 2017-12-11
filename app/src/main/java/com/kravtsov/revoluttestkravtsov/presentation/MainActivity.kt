package com.kravtsov.revoluttestkravtsov.presentation

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.kravtsov.revoluttestkravtsov.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CurrenciesViewModel
    private val viewModelSubscriptions = CompositeDisposable()
    private var currenciesRecyclerAdapter: CurrenciesRecyclerAdapter? = null

    //// ACTIVITY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupCurrenciesRecyclerView()
        viewModel = ViewModelFactory.provideCurrenciesViewModel()
    }

    override fun onStart() {
        super.onStart()
        viewModelSubscriptions.add(viewModel.getViewStateUpdates().subscribe { viewState -> render(viewState) })
        viewModel.onViewAppeared()
    }

    override fun onStop() {
        super.onStop()
        viewModelSubscriptions.clear()
        viewModel.onViewDisappeared()
    }

    ////

    private fun setupCurrenciesRecyclerView() {
        currenciesRecyclerAdapter = CurrenciesRecyclerAdapter(currenciesRecyclerView) { currency ->
            viewModel.onCurrencySelected(currency)

            Log.d("LOG", "MAIN")

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
        currenciesRecyclerView.layoutManager = LinearLayoutManager(this)
        currenciesRecyclerView.setHasFixedSize(true)
        currenciesRecyclerView.adapter = currenciesRecyclerAdapter
    }

    private fun render(viewState: CurrenciesViewState) {
        if (viewState.loading) loadingProgressBar.visibility = View.VISIBLE
        else {
            loadingProgressBar.visibility = View.GONE
            viewState.currencies?.let { currenciesRecyclerAdapter?.update(it) }
        }
    }
}






















