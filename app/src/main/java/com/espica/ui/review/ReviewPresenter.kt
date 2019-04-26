package com.espica.ui.home

import com.espica.data.network.ApiClient
import com.espica.data.network.MyDisposableObserver
import com.espica.data.network.response.DefaultResponse
import io.reactivex.disposables.CompositeDisposable

class ReviewPresenter(val apiClient: ApiClient) : ExerciseContract.Presenter {
    val compositeDisposable = CompositeDisposable()
    var view: ExerciseContract.View? = null

    override fun loadVideos() {
        view?.showLoading()
        compositeDisposable.add(apiClient.allVideos.subscribeWith(object : MyDisposableObserver<DefaultResponse>() {
            override fun onSuccess(t: DefaultResponse) {
                view?.hideLoading()
            }
        }
        ))
    }

    override fun destroy() {
        compositeDisposable.clear()
    }
}