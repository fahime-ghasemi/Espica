package com.espica.ui.home

import com.espica.data.model.Video

interface VideoItemListener {
    fun onPlayVideoClick(video: Video)
}