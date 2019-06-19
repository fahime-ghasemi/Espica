package com.espica.ui.home

import com.espica.data.model.Video
import com.espica.data.network.response.VideoItem

interface VideoItemListener {
    fun onPlayVideoClick(video: VideoItem)
}