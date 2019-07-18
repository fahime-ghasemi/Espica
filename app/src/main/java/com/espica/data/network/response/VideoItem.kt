package com.espica.data.network.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class VideoItem(
    @SerializedName("video_name")
    var name: String, @SerializedName("video_id")
    var id: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("duration")
    var duration: String,
    @SerializedName("image")
    var image: String
) : Parcelable {

    val downloadName: String = id.toString() + "_" + title

    companion object
    {
        val DOWNLOADED = 1
        val DOWNLOADING = 2
        val NOT_DOWNLOADED = 3
    }
}
