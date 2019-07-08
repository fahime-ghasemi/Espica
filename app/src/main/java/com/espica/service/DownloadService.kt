package com.espica.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import android.widget.VideoView
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.espica.R
import com.espica.data.BundleKeys
import com.espica.data.network.Url
import com.espica.data.network.response.VideoItem
import kotlinx.android.synthetic.main.exo_playback_control_view.*

class DownloadService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val videoItem = intent!!.getParcelableExtra<VideoItem>(BundleKeys.VIDEO)

        PRDownloader.download(
            Url.BASE_URL + videoItem?.name,
            filesDir.path,
            videoItem?.downloadName
        )
            .build()
            .setOnPauseListener {

            }
            .setOnProgressListener {
//                download.setImageResource(R.drawable.ic_download)
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    Toast.makeText(applicationContext, "downlaod complete", Toast.LENGTH_LONG)
//                    download.setImageResource(R.drawable.ic_file_download)
                }

                override fun onError(error: Error?) {
                    Toast.makeText(applicationContext, "downlaod error", Toast.LENGTH_LONG)

                }

            })
        return START_NOT_STICKY
    }
}
