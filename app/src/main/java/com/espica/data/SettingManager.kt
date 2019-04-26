package com.espica.data

import android.content.Context
import android.content.SharedPreferences
import android.media.session.MediaSession

class SettingManager(context: Context)
{
    private val LOGIN_STATUS = "login_status"
    private val TOKEN = "token"

    private val mSharedPreferences = context.getSharedPreferences("espica", Context.MODE_PRIVATE)

    private val mEditor= mSharedPreferences.edit()

    fun setLoginStatus(status: Boolean) {
        mEditor.putBoolean(LOGIN_STATUS, status).apply()
    }

    fun isLogin(): Boolean {
        return mSharedPreferences.getBoolean(LOGIN_STATUS, false)
    }
    fun setToken(token:String)
    {
        mEditor.putString(TOKEN,token)
    }
    fun getToken():String?
    {
        return mSharedPreferences.getString(TOKEN,"")
    }
}