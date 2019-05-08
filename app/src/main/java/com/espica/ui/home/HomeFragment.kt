package com.espica.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.espica.BaseFragment
import com.espica.EspicaApp
import com.espica.R
import com.espica.data.network.ApiClient
import com.espica.ui.adapter.VideoAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.loading.*

class HomeFragment : BaseFragment(), HomeContract.View {

    lateinit var presenter: HomePresenter
    override val layoutResId = R.layout.fragment_home

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = HomePresenter(ApiClient((activity!!.application as EspicaApp).networkApiService))

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        mListener?.onNewFragmentAttached(2)
        presenter.view = this
        presenter.loadVideos()

    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager =linearLayoutManager
        recyclerView.addOnScrollListener(ScrollListener(linearLayoutManager,presenter))
        recyclerView.adapter = VideoAdapter(ArrayList())

    }

    override fun hideLoading() {
        loading.visibility = View.GONE
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

}

class ScrollListener(mLinearLayoutManager: LinearLayoutManager,var presenter: HomePresenter) : EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
    override fun onLoadMore(current_page: Int) {
        presenter.loadNextVideos()
    }
}

