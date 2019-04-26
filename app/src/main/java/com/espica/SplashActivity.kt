package com.espica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.espica.data.SettingManager
import com.espica.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    lateinit var settingManager :SettingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingManager = SettingManager(this)
//        setContentView(R.layout.activity_splash)
//        if(!settingManager.isLogin())
//            goToLogin()
//        else goToMain()
        goToMain()
        finish()
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
