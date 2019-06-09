package com.espica.ui.login

interface LoginActivityListener {
    fun onLoginWithPhone()
    fun onSmsSent(mobile:String)

}