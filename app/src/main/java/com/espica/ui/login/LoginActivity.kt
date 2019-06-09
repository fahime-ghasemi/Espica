package com.espica.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.espica.R
import com.espica.data.BundleKeys

class LoginActivity : AppCompatActivity() , LoginActivityListener {
    override fun onSmsSent(mobile:String) {
        val bundle = Bundle()
        bundle.putString(BundleKeys.MOBILE,mobile)
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayout,
            SendCodeFragment.newInstance(bundle)).addToBackStack("sendCode").commit()
    }

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
