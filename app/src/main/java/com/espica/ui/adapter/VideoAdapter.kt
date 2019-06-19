package com.espica.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.espica.data.model.Video
import com.espica.data.network.response.VideoItem
import com.espica.ui.home.VideoItemListener
import kotlinx.android.synthetic.main.item_video.view.*

class VideoAdapter(private val videoList: ArrayList<VideoItem>,private val listener: VideoItemListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_TYPE = 1
    val PROGRESS_TYPE = 2
    var showProgress = false

    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val title = view.videoTitle
        val duration = view.videoDuration

    }

    class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == PROGRESS_TYPE) {
            val view =
                LayoutInflater.from(parent.context).inflate(com.espica.R.layout.item_progress, parent, false)
            return VideoAdapter.ProgressViewHolder(view)
        }
        val view = LayoutInflater.from(parent.context).inflate(com.espica.R.layout.item_video, parent, false)
        return VideoAdapter.VideoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (showProgress) videoList.size + 1 else videoList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != videoList.size) {
            val video = videoList[position]
            val videoViewHolder = holder as VideoViewHolder
            videoViewHolder.title.text = video.title
            videoViewHolder.title.setOnClickListener {
                listener.onPlayVideoClick(video)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == videoList.size) PROGRESS_TYPE else ITEM_TYPE
    }

    fun addItmes(videoList: List<VideoItem>) {
        this.videoList.addAll(videoList)
    }
}