package com.espica.data.network


import com.espica.BuildConfig

object Url {
    //192.168.134.11
    //37.130.202.169
    private val BASE_URL_TEST = "http://185.86.36.76/api/"
    private val BASE_URL_PRODUCTION = "http://185.86.36.76/api/"
    val BASE_URL = if (BuildConfig.DEBUG) Url.BASE_URL_TEST else Url.BASE_URL_PRODUCTION
    const val REGISTER_WITH_PHONE = "registerDevice/device/"
    const val REGISTER_DEVICE = "register/device/"
    const val GET_LEITNER = "show/litner/"
    const val GET_ALL_VIDEOS = "video/list/"
    const val SEND_OTP = "send/otp/"
    const val VERIFY_CODE = "verify/otp/"
    const val ADD_TO_LEITNER = "add/flashcard/"

}
