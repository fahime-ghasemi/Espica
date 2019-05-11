package com.espica.ui.home

import com.espica.data.model.Video
import com.espica.data.network.ApiClient
import io.reactivex.disposables.CompositeDisposable

class HomePresenter(val apiClient: ApiClient) : HomeContract.Presenter {
    val compositeDisposable = CompositeDisposable()
    var view: HomeContract.View? = null

    override fun loadVideos(loadNextVideos: Boolean) {
        if (!loadNextVideos)
            view?.showLoading()
//        compositeDisposable.add(apiClient.allVideos.subscribeWith(object : MyDisposableObserver<DefaultResponse>() {
//            override fun onSuccess(t: DefaultResponse) {
//                view?.hideLoading()
//            }
//        }
//        ))
        val videoList = ArrayList<Video>()
        videoList.add(Video("", "video1"))
        videoList.add(Video("", "video2"))
        videoList.add(Video("", "video3"))
        videoList.add(Video("", "video4"))
        videoList.add(Video("", "video5"))
        view?.hideLoading()
        view?.showVideos(videoList, true)
    }

    override fun destroy() {
        compositeDisposable.clear()
    }


}