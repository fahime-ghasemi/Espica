package com.espica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationItem = AHBottomNavigationItem(getString(R.string.home),R.drawable.ic_home)
        bottomNavigation.addItem(bottomNavigationItem)
//        bottomNavigation.add
    }
}
