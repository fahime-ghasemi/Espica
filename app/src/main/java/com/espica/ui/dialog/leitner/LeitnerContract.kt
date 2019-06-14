package com.espica.ui.dialog.leitner

import com.espica.BasePresenter
import com.espica.BaseView

interface LeitnerContract
{
    interface AddToLeitnerView :BaseView
    {

    }

    interface Presenter:BasePresenter
    {
        fun addToLeitner(phrase: String, desc: String, userId: String)
    }
}