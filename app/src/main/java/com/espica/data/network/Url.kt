package com.espica.data.network


import com.espica.BuildConfig

object Url {
    //192.168.134.11
    //37.130.202.169
    private val BASE_URL_TEST = "http://66.70.130.225:3010/api/clients/"
    private val BASE_URL_PRODUCTION = "http://66.70.130.225:3010/api/clients/"
    val BASE_URL = if (BuildConfig.DEBUG) Url.BASE_URL_TEST else Url.BASE_URL_PRODUCTION
    const val REGISTER_WITH_PHONE = "{client_id}/register/{nonce}"

}
