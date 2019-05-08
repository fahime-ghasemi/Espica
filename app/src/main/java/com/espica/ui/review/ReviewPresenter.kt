package com.espica.ui.home

import com.espica.data.network.ApiClient
import io.reactivex.disposables.CompositeDisposable

class ReviewPresenter(val apiClient: ApiClient) : ReviewContract.Presenter {
    val compositeDisposable = CompositeDisposable()
    var view: ReviewContract.View? = null

    override fun destroy() {
        compositeDisposable.clear()
    }
}