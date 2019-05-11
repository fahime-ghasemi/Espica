package com.espica.ui.home

import com.espica.data.network.ApiClient
import com.espica.data.network.MyDisposableObserver
import com.espica.data.network.response.DefaultResponse
import io.reactivex.disposables.CompositeDisposable

class ExercisePresenter(val apiClient: ApiClient) : ExerciseContract.Presenter {
    val compositeDisposable = CompositeDisposable()
    var view: ExerciseContract.View? = null


    override fun destroy() {
        compositeDisposable.clear()
    }
}