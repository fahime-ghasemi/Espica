package com.espica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

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
