package com.espica.data

import android.content.Context
import android.content.SharedPreferences

class SettingManager
{
    private val LOGIN_STATUS = "login_status"
    private val STATUS = "status"
    private val CLIENT_ID = "client_id"
    private val PRIVATE_KEY = "private_key"
    private val PUBLIC_KEY = "public_key"
    private val PREPARE_STATUS = "prepare_status"
    private val FIRST_NAME = "firstName"
    private val LAST_NAME = "lastName"
    private val AVATAR = "avatar"
    private val NATIONAL_CODE = "national_code"
    private val PHONE = "phone"
    private val BIRTH_DATE = "birth_date"
    private val ACCESS_TOKEN = "access_token"
    private val FILE_EXTENSION = "extension"
    private var mEditor: SharedPreferences.Editor
    private var mSharedPreferences: SharedPreferences

    fun SettingManager(context: Context){
        mSharedPreferences = context.getSharedPreferences("block-chain", Context.MODE_PRIVATE)
        mEditor = mSharedPreferences.edit()
    }

    fun setLoginStatus(status: Boolean) {
        mEditor.putBoolean(LOGIN_STATUS, status).apply()
    }

    fun isLogin(): Boolean {
        return mSharedPreferences.getBoolean(LOGIN_STATUS, false)
    }
}