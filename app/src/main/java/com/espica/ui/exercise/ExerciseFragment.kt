package com.espica.ui.home

import android.os.Bundle
import android.view.View
import com.espica.BaseFragment
import com.espica.EspicaApp
import com.espica.R
import com.espica.data.network.ApiClient
import kotlinx.android.synthetic.main.loading.*

class ExerciseFragment : BaseFragment(), ExerciseContract.View {

    lateinit var presenter: ExercisePresenter

    override val layoutResId = R.layout.fragment_exercise

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

    }

    override fun hideLoading() {
        loading.visibility = View.GONE
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

}