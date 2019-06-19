package com.espica.ui.home

import com.espica.BasePresenter
import com.espica.BaseView
import com.espica.data.model.Video
import com.espica.data.network.response.VideoItem

interface HomeContract {
    interface View : BaseView {
        fun showVideos(videoList: List<VideoItem>, hasMoreVideo: Boolean)
    }

    interface Presenter : BasePresenter {
        fun loadVideos(pageNumber: Int)
//        fun loadNextVideos()
    }
}