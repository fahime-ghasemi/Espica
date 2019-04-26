package com.espica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.espica.data.model.MenuItem
import com.espica.ui.home.ExerciseFragment
import com.espica.ui.adapter.MenuAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.*

class MainActivity : AppCompatActivity(),MainActivityListener {

    val menuItems = listOf(
        MenuItem(R.string.fav, R.drawable.ic_favorite),
        MenuItem(R.string.download, R.drawable.ic_download),
        MenuItem(R.string.setting, R.drawable.ic_settings),
        MenuItem(R.string.about_us, R.drawable.ic_about_us),
        MenuItem
            (R.string.invite, R.drawable.ic_contact_us)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    private fun initUI() {
        val bottomNavigationItemHome = AHBottomNavigationItem(getString(R.string.home), R.drawable.ic_home)
        val bottomNavigationItemReview = AHBottomNavigationItem(getString(R.string.review), R.drawable.ic_review)
        val bottomNavigationItemExercise = AHBottomNavigationItem(getString(R.string.exercise), R.drawable.ic_exercise)
        bottomNavigation.addItem(bottomNavigationItemHome)
        bottomNavigation.addItem(bottomNavigationItemReview)
        bottomNavigation.addItem(bottomNavigationItemExercise)
        menu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }
        listViewMenu.adapter = MenuAdapter(this, menuItems)
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, ExerciseFragment.newInstance()).commit()
    }

    override fun onNewFragmentAttached(fragmentPosition: Int) {
    }
}
