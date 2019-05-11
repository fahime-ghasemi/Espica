package com.espica.ui.home

import com.espica.BasePresenter
import com.espica.BaseView
import com.espica.data.model.Video

interface HomeContract {
    interface View : BaseView {
        fun showVideos(videoList: ArrayList<Video>, hasMoreVideo: Boolean)
    }

    interface Presenter : BasePresenter {
        fun loadVideos(loadNextVideos: Boolean = false)
//        fun loadNextVideos()
    }
}