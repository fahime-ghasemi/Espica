package com.espica.ui.home

import com.espica.data.model.Video
import com.espica.data.network.ApiClient
import com.espica.data.network.MyDisposableObserver
import com.espica.data.network.response.DefaultResponse
import com.espica.data.network.response.VideoListResponse
import io.reactivex.disposables.CompositeDisposable

class HomePresenter(val apiClient: ApiClient) : HomeContract.Presenter {
    val compositeDisposable = CompositeDisposable()
    var view: HomeContract.View? = null

    override fun loadVideos(loadNextVideos: Boolean) {
        if (!loadNextVideos)
            view?.showLoading()
        compositeDisposable.add(apiClient.allVideos.subscribeWith(object : MyDisposableObserver<VideoListResponse>() {
            override fun onSuccess(response: VideoListResponse) {
                view?.hideLoading();
            }

        }
        ))
//        val videoList = ArrayList<Video>()
//        videoList.add(Video("", "video1"))
//        videoList.add(Video("", "video2"))
//        videoList.add(Video("", "video3"))
//        videoList.add(Video("", "video4"))
//        videoList.add(Video("", "video5"))
//        leitnerView?.hideLoading()
//        leitnerView?.showVideos(videoList, true)
    }

    override fun destroy() {
        compositeDisposable.clear()
    }


}