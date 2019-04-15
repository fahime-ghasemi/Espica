package com.espica.data

import android.content.Context
import android.content.SharedPreferences

class SettingManager(context: Context)
{
    private val LOGIN_STATUS = "login_status"
    private val mSharedPreferences = context.getSharedPreferences("block-chain", Context.MODE_PRIVATE)

    private val mEditor= mSharedPreferences.edit()

    fun setLoginStatus(status: Boolean) {
        mEditor.putBoolean(LOGIN_STATUS, status).apply()
    }

    fun isLogin(): Boolean {
        return mSharedPreferences.getBoolean(LOGIN_STATUS, false)
    }
}