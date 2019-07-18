package com.espica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.espica.data.BundleKeys
import com.espica.data.EspicaManager
import com.espica.data.model.MenuItem
import com.espica.ui.home.ExerciseFragment
import com.espica.ui.adapter.MenuAdapter
import com.espica.ui.leitner.AddToLeitnerDialog
import com.espica.ui.dialog.FilterDialogFragment
import com.espica.ui.home.HomeFragment
import com.espica.ui.home.OnMenuItemClickListener
import com.espica.ui.home.ReviewFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.*

class MainActivity : AppCompatActivity(), MainActivityListener, OnMenuItemClickListener {
    var baseFragment: BaseFragment? = null
    var currentFragment: Int? = FRAGMENT_VIDEO_LIST

    companion object {
        val FRAGMENT_REVIEW = 2
        val FRAGMENT_VIDEO_LIST = 1
        val FRAGMENT_EXCERCISE = 3
    }

    val menuItems = listOf(
        MenuItem(1, R.string.home, R.drawable.ic_home),
        MenuItem(2, R.string.review, R.drawable.ic_review),
        MenuItem(3, R.string.exercise, R.drawable.ic_exercise),
        MenuItem(4, R.string.fav, R.drawable.ic_favorite),
        MenuItem(5, R.string.download, R.drawable.ic_file_download),
        MenuItem(6, R.string.setting, R.drawable.ic_settings),
        MenuItem(7, R.string.about_us, R.drawable.ic_about_us),
        MenuItem
            (8, R.string.invite, R.drawable.ic_contact_us)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        if (savedInstanceState != null)
            currentFragment = savedInstanceState.getInt(BundleKeys.CURRENT_FRAGMENT, FRAGMENT_VIDEO_LIST)
        loadFragment(currentFragment)
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
//        bottomNavigation.currentItem = 2
        menu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }
        listViewMenu.adapter = MenuAdapter(this, menuItems, this)
        initActionMenu()
    }

    private fun initActionMenu() {
        filter.setOnClickListener {
            val dialogFragment = FilterDialogFragment()
            dialogFragment.show(supportFragmentManager, null)
        }
        add_g5.setOnClickListener {
            val dialogFragment = AddToLeitnerDialog();
            dialogFragment.show(supportFragmentManager, "leitner")
        }
    }

    override fun OnMenuItemClick(item: MenuItem) {
        when (item.id) {
            1 -> {
                loadFragment(1)
            }
            2 -> {
                loadFragment(2)
            }
            3 -> {
                loadFragment(3)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.END)
    }

    private fun loadFragment(tab: Int?) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        when (tab) {
            1 -> {
                baseFragment = supportFragmentManager.findFragmentByTag("home") as? BaseFragment
                if (baseFragment == null)
                    baseFragment = HomeFragment.newInstance()
                currentFragment = FRAGMENT_VIDEO_LIST
                fragmentTransaction.replace(R.id.frameLayout, baseFragment!!, "home")
                    .commit()
            }
            2 -> {
                baseFragment = supportFragmentManager.findFragmentByTag("review") as? BaseFragment
                if (baseFragment == null)
                    baseFragment = ReviewFragment.newInstance()
                currentFragment = FRAGMENT_REVIEW
                fragmentTransaction.replace(R.id.frameLayout, baseFragment!!, "review")
                    .commit()
            }
            3 -> {
                baseFragment = supportFragmentManager.findFragmentByTag("exercise") as? BaseFragment
                if (baseFragment == null)
                    baseFragment = ExerciseFragment.newInstance()
                currentFragment = FRAGMENT_EXCERCISE
                fragmentTransaction.replace(R.id.frameLayout, baseFragment!!, "exercise")
                    .commit()
            }
        }
    }

    override fun onNewFragmentAttached(fragment: Int) {
//        bottomNavigation.currentItem = fragmentPosition
        if (fragment == FRAGMENT_REVIEW) {
            filter.visibility = View.GONE
            add_g5.visibility = View.VISIBLE
        } else if (fragment == FRAGMENT_VIDEO_LIST) {
            filter.visibility = View.VISIBLE
            add_g5.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(BundleKeys.CURRENT_FRAGMENT, currentFragment!!)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        EspicaManager.getInstance(applicationContext).release()
        super.onDestroy()
    }
}
