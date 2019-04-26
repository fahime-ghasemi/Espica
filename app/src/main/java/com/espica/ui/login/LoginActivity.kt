package com.espica.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.espica.R

class LoginActivity : AppCompatActivity() , LoginActivityListener {
    override fun onLoginWithPhone() {
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayout,
            GetPhoneFragment.newInstance()).addToBackStack("getPhone").commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, LoginFragment.newInstance())
            .commit()
    }
}
