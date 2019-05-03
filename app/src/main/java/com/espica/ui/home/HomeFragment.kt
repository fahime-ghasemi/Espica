package com.espica.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.espica.BaseFragment
import com.espica.EspicaApp
import com.espica.R
import com.espica.data.model.Video
import com.espica.data.network.ApiClient
import com.espica.ui.adapter.VideoAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.loading.*

class HomeFragment : BaseFragment(), ExerciseContract.View {

    lateinit var presenter: ExercisePresenter

    override val layoutResId = R.layout.fragment_home

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ExercisePresenter(ApiClient((activity!!.application as EspicaApp).networkApiService))
        initRecyclerView()

    }

    private fun initRecyclerView() {
        recyclerView.adapter = VideoAdapter(ArrayList())
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener?.onNewFragmentAttached(2)
        presenter.view = this
        presenter.loadVideos()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun hideLoading() {
        loading.visibility = View.GONE
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

}