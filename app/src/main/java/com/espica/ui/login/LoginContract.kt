package com.espica.ui.login

import com.espica.BasePresenter
import com.espica.BaseView

interface LoginContract {
    interface GetPhoneView: BaseView
    {
        fun showVerifyPage()
        fun showError(message: String?)

    }

    interface SendCodeView :BaseView
    {
        fun showMainPage()
        fun saveUserInfo(userId: Int?)
        fun showError(error_code: Int)
    }

    interface Presenter:BasePresenter
    {
        fun sendPhone(phone:String)
        fun verifyCode(code: String, mobile: String?)
    }
}