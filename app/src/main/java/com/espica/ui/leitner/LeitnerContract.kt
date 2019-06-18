package com.espica.ui.leitner

import com.espica.BasePresenter
import com.espica.BaseView
import com.espica.data.network.response.LeitnerCard

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
        fun review(id: Int?, quality: Int)

    }

    interface LeitnerView:BaseView {
        fun showLeitnerData(items: List<LeitnerCard>?)
        fun showNextItem()
    }
}