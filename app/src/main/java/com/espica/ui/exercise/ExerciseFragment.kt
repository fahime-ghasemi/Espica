package com.espica.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.espica.BaseFragment
import com.espica.EspicaApp
import com.espica.R
import com.espica.data.network.ApiClient
import kotlinx.android.synthetic.main.fragment_exercise.*
import kotlinx.android.synthetic.main.loading.*
import android.graphics.Point
import android.util.DisplayMetrics
import android.util.Log
import com.espica.tools.Utils


class ExerciseFragment : BaseFragment(), ExerciseContract.View {

    lateinit var presenter: ExercisePresenter

    override val layoutResId = com.espica.R.layout.fragment_exercise

    companion object {
        fun newInstance(): ExerciseFragment {
            return ExerciseFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ExercisePresenter(ApiClient((activity!!.application as EspicaApp).networkApiService))

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener?.onNewFragmentAttached(0)
        presenter.view = this
        initGridLayout()
        addCard()
    }

    override fun hideLoading() {
        loading.visibility = View.GONE
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    fun initGridLayout() {
        val display = activity!!.windowManager.getDefaultDisplay()
        val size = DisplayMetrics()
        display.getMetrics(size);
        val distance =
            resources.getDimensionPixelSize(R.dimen.grid_cell_distance)

        val cardArea =
            resources.getDimensionPixelSize(R.dimen.card_width) + (2* resources.getDimensionPixelSize(R.dimen.card_padding))

        val columnCount = size.widthPixels / cardArea

        gridLayout.columnCount = columnCount.toInt()
    }

    fun addCard() {

        val view = LayoutInflater.from(context).inflate(com.espica.R.layout.item_grid_card, null)
        gridLayout.addView(view)

        val view2 = LayoutInflater.from(context).inflate(com.espica.R.layout.item_grid_card, null)
        gridLayout.addView(view2)

        val view3 = LayoutInflater.from(context).inflate(com.espica.R.layout.item_grid_card, null)
        gridLayout.addView(view3)

        val view4 = LayoutInflater.from(context).inflate(com.espica.R.layout.item_grid_card, null)
        gridLayout.addView(view4)

    }


}