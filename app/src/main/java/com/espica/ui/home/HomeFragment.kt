package com.espica.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.espica.BaseFragment
import com.espica.EspicaApp
import com.espica.MainActivity
import com.espica.ui.player.PlayerActivity
import com.espica.R
import com.espica.data.BundleKeys
import com.espica.data.model.Video
import com.espica.data.network.ApiClient
import com.espica.data.network.response.VideoItem
import com.espica.ui.adapter.VideoAdapter
import com.espica.ui.dialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.loading.*

class HomeFragment : BaseFragment(), HomeContract.View ,VideoItemListener{

    lateinit var presenter: HomePresenter
    override val layoutResId = R.layout.fragment_home
    var hasMoreVideo = true
    var progress : ProgressDialog? = null
    var pageNumber = 1

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
        mListener?.onNewFragmentAttached(MainActivity.FRAGMENT_VIDEO_LIST)
        presenter.view = this
        presenter.loadVideos(pageNumber)

    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addOnScrollListener(ScrollListener(linearLayoutManager))
        recyclerView.adapter = VideoAdapter(ArrayList(),this)

    }

    override fun hideLoading() {
        progress?.dismiss()
    }

    override fun showLoading() {
        progress = ProgressDialog.newInstance()
        progress?.show(childFragmentManager, "")
    }

    override fun showVideos(videoList: List<VideoItem>, hasMoreVideo: Boolean) {
        this.hasMoreVideo = hasMoreVideo
        if(hasMoreVideo) pageNumber++
        val position = recyclerView.adapter!!.itemCount
        (recyclerView.adapter as VideoAdapter).addItmes(videoList)
        recyclerView.post(object : Runnable {
            override fun run() {
                recyclerView.adapter!!.notifyItemInserted(position)
            }
        })

    }

    override fun onPlayVideoClick(video: VideoItem) {
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra(BundleKeys.VIDEO,video)
        startActivity(intent)
    }

    inner class ScrollListener(mLinearLayoutManager: LinearLayoutManager) :
        EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
        override fun onLoadMore(current_page: Int) {
            if (hasMoreVideo)
                presenter.loadVideos(pageNumber)
        }
    }
}