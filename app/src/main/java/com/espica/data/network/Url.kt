package com.espica.data.network


import com.espica.BuildConfig

object Url {
    //192.168.134.11
    //37.130.202.169
    private val BASE_URL_TEST = "http://185.86.36.76/"
    private val BASE_URL_PRODUCTION = "http://185.86.36.76/"
    public val BASE_URL = if (BuildConfig.DEBUG) Url.BASE_URL_TEST else Url.BASE_URL_PRODUCTION
    const val REGISTER_DEVICE = "api/register/device/"
    const val GET_LEITNER = "api/show/litner/"
    const val GET_ALL_VIDEOS = "api/video/list/"
    const val SEND_OTP = "api/send/otp/"
    const val VERIFY_CODE = "api/verify/otp/"
    const val ADD_TO_LEITNER = "api/add/flashcard/"
    const val LEITNER_REVIEW = "api/flashcard/review/"
    const val SRT_FILE = "api/srt/download/"

}
