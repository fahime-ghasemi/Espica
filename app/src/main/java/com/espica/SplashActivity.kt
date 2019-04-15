package com.espica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.espica.data.SettingManager

class SplashActivity : AppCompatActivity() {

    lateinit var settingManager :SettingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingManager = SettingManager(this)
//        setContentView(R.layout.activity_splash)
        if(settingManager.isLogin())
            goToLogin()
        else goToLogin()
    }

    fun goToMain()
    {
        val intent = Intent()
        intent.setClass(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun goToLogin()
    {
        val intent = Intent()
        intent.setClass(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
