package com.espica.ui.leitner

import com.espica.BasePresenter
import com.espica.BaseView

interface LeitnerContract
{
    interface AddToLeitnerView :BaseView
    {
        fun showToast(message_add_to_leitner: Int)

    }

    interface Presenter:BasePresenter
    {
        fun addToLeitner(phrase: String, desc: String, userId: String)
        fun getLeitnerData(userId: String)

    }

    interface LeitnerView:BaseView {
        fun showLeitnerData()
    }
}