package com.espica.data.network.response

import com.google.gson.annotations.SerializedName

class VideoListResponse {
    @SerializedName("count")
    var count: Int = 0

    @SerializedName("next")
    var next : String? =null

    @SerializedName("previous")
    var previous : String? = null

    @SerializedName("results")
    var results : List<VideoItem> = ArrayList()

}
