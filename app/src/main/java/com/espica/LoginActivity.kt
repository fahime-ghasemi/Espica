package com.espica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.espica.ui.SendCodeFragment

class LoginActivity : AppCompatActivity() {

    var fragmentManager: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        fragmentManager.beginTransaction().replace(R.id.frameLayout, SendCodeFragment.newInstance())
    }
}
