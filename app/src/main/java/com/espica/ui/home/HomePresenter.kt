package com.espica.ui.home

import com.espica.data.network.ApiClient
import com.espica.data.network.MyDisposableObserver
import com.espica.data.network.response.VideoListResponse
import io.reactivex.disposables.CompositeDisposable

class HomePresenter(val apiClient: ApiClient) : HomeContract.Presenter {
    val compositeDisposable = CompositeDisposable()
    var view: HomeContract.View? = null

    override fun loadVideos(offset: Int) {
//        if (offset == 0)
//            view?.showLoading()
        compositeDisposable.add(apiClient.getAllVideos(offset).subscribeWith(object :
            MyDisposableObserver<VideoListResponse>() {
            override fun onSuccess(response: VideoListResponse) {
//                view?.hideLoading();
                view?.showVideos(response.results, response.next != null)
            }
        }
        ))
    }

    override fun destroy() {
        compositeDisposable.clear()
    }


}