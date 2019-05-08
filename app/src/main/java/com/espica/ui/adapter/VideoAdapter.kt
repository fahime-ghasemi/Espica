package com.espica.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.espica.data.model.Video

class VideoAdapter(val videoList: List<Video>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_TYPE = 1
    val PROGRESS_TYPE = 2
    var showProgress = false

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == PROGRESS_TYPE) {
            val view =
                LayoutInflater.from(parent.context).inflate(com.espica.R.layout.item_progress, parent, false)
            return VideoAdapter.ProgressViewHolder(view)
        }
        val view = LayoutInflater.from(parent.context).inflate(com.espica.R.layout.item_video, parent, false)
        return VideoAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (showProgress) videoList.size + 1 else videoList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val video = videoList[position]

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == videoList.size) PROGRESS_TYPE else ITEM_TYPE
    }

}