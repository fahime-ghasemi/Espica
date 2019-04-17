package com.espica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.espica.ui.GetPhoneFragment
import com.espica.ui.LoginActivityListener
import com.espica.ui.LoginFragment

class LoginActivity : AppCompatActivity() , LoginActivityListener {
    override fun onLoginWithPhone() {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,GetPhoneFragment.newInstance()).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, LoginFragment.newInstance())
            .commit()
    }
}
