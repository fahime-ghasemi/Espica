package com.espica.ui.home

import com.espica.BasePresenter
import com.espica.BaseView

interface ExerciseContract {
    interface View : BaseView {
//        showAllVideos()
    }

    interface Presenter : BasePresenter {
        fun loadVideos()
    }
}