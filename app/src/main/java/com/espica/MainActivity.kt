package com.espica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TabHost
import androidx.core.view.GravityCompat
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.espica.data.model.MenuItem
import com.espica.ui.home.ExerciseFragment
import com.espica.ui.adapter.MenuAdapter
import com.espica.ui.dialog.DialogFragment
import com.espica.ui.home.HomeFragment
import com.espica.ui.home.ReviewFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.android.synthetic.main.toolbar_main.view.*

class MainActivity : AppCompatActivity(), MainActivityListener {
    var baseFragment: BaseFragment? = null

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
        bottomNavigation.addItem(bottomNavigationItemExercise)
        bottomNavigation.addItem(bottomNavigationItemReview)
        bottomNavigation.addItem(bottomNavigationItemHome)
        bottomNavigation.setOnTabSelectedListener(object : AHBottomNavigation.OnTabSelectedListener {
            override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
                if (wasSelected) return true
                loadFragment(position)
                return true
            }

        })
        bottomNavigation.currentItem = 2
        menu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }
        listViewMenu.adapter = MenuAdapter(this, menuItems)
        filter.setOnClickListener {
            val dialogFragment = DialogFragment()
            dialogFragment.show(supportFragmentManager,null)
        }
    }

    private fun loadFragment(tab: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        when (tab) {
            2 -> {
                baseFragment = supportFragmentManager.findFragmentByTag("home") as? BaseFragment
                if (baseFragment == null)
                    baseFragment = HomeFragment.newInstance()
                fragmentTransaction.replace(R.id.frameLayout, baseFragment!!, "home")
                    .commit()
            }
            1 -> {
                baseFragment = supportFragmentManager.findFragmentByTag("review") as? BaseFragment
                if (baseFragment == null)
                    baseFragment = ReviewFragment.newInstance()
                fragmentTransaction.replace(R.id.frameLayout, baseFragment!!, "review")
                    .commit()
            }
            0 -> {
                baseFragment = supportFragmentManager.findFragmentByTag("exercise") as? BaseFragment
                if (baseFragment == null)
                    baseFragment = ExerciseFragment.newInstance()
                fragmentTransaction.replace(R.id.frameLayout, baseFragment!!, "exercise")
                    .commit()
            }
        }
    }

    override fun onNewFragmentAttached(fragmentPosition: Int) {
        bottomNavigation.currentItem = fragmentPosition
    }
}
