package com.espica.ui.home

import com.espica.data.network.ApiClient
import com.espica.data.network.MyDisposableObserver
import com.espica.data.network.response.DefaultResponse
import io.reactivex.disposables.CompositeDisposable

class HomePresenter(val apiClient: ApiClient) : HomeContract.Presenter {
    val compositeDisposable = CompositeDisposable()
    var view: HomeContract.View? = null

    override fun loadVideos() {
        view?.showLoading()
//        compositeDisposable.add(apiClient.allVideos.subscribeWith(object : MyDisposableObserver<DefaultResponse>() {
//            override fun onSuccess(t: DefaultResponse) {
//                view?.hideLoading()
//            }
//        }
//        ))
    }

    override fun destroy() {
        compositeDisposable.clear()
    }


}