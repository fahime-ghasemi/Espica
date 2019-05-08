package com.espica.ui.home

import com.espica.BasePresenter
import com.espica.BaseView
import com.espica.data.model.Video

interface ExerciseContract {
    interface View : BaseView {
        fun showVideos(videoList: List<Video>)
    }

    interface Presenter : BasePresenter {

    }
}