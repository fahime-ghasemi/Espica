package com.espica.ui.login

import com.espica.BasePresenter
import com.espica.BaseView

interface LoginContract {
    interface GetPhoneView: BaseView
    {
        fun showVerifyPage()

    }

    interface SendCodeView :BaseView
    {
        fun showMainPage()
    }

    interface Presenter:BasePresenter
    {
        fun sendPhone(phone:String)
        fun verifyCode(code: String, mobile: String?)
    }
}