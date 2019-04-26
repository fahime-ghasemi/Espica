package com.espica.ui.home

import com.espica.BasePresenter
import com.espica.BaseView

interface HomeContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter {
        fun loadVideos();
    }
}