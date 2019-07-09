package com.espica.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.Status
import com.espica.data.BundleKeys
import com.espica.data.network.Url
import com.espica.data.network.response.VideoItem
import com.espica.ui.player.PlayerActivity

class DownloadService : Service() {

    val TAG = "DownloadService"
    val downlodList = ArrayList<Int>()
    companion object
    {
        val STATUS_DOWNLOADING = 1
        val STATUS_FINISHED = 0
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
        val receiverIntent = Intent(baseContext, PlayerActivity.DownloadReceiver::class.java)
        receiverIntent.action = "downlaod"
        val videoItem = intent!!.getParcelableExtra<VideoItem>(BundleKeys.VIDEO)
        Thread().run {
            val downloadId = PRDownloader.download(
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
                .setOnStartOrResumeListener {
                    receiverIntent.putExtra(BundleKeys.DOWNLOAD_STATUS, STATUS_DOWNLOADING)
                    LocalBroadcastManager.getInstance(baseContext).sendBroadcast(receiverIntent)
                    //todo notification
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        Log.i(TAG, "downlaod complete")
//                    download.setImageResource(R.drawable.ic_file_download)
                        receiverIntent.putExtra(BundleKeys.DOWNLOAD_STATUS, STATUS_FINISHED)
                        LocalBroadcastManager.getInstance(baseContext).sendBroadcast(receiverIntent)
                        if (allDownloadFinished()) stopSelf()
                    }

                    override fun onError(error: Error?) {
                        Log.i(TAG, "downlaod error")

                    }

                })
            downlodList.add(downloadId)
        }
        return START_NOT_STICKY
    }

    fun allDownloadFinished(): Boolean {
        downlodList.forEach {
            if (PRDownloader.getStatus(it) != Status.COMPLETED)
                return false
        }
        Log.i(TAG,"allDownloadFinished")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }
}
