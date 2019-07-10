package com.espica.service

import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.Status
import com.espica.data.BundleKeys
import com.espica.data.network.Url
import com.espica.data.network.response.VideoItem
import com.espica.ui.player.PlayerActivity
import com.espica.tools.Utils
import android.R.attr.colorPrimary
import androidx.core.content.ContextCompat
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat.getContentTitle
import io.fabric.sdk.android.services.settings.IconRequest.build
import android.R
import android.app.Notification
import android.util.SparseIntArray
import androidx.core.util.forEach


class DownloadService : Service() {

    val TAG = "DownloadService"
    val downlodList = ArrayList<Int>()
    lateinit var mNotificationManagerCompat: NotificationManagerCompat
    var notificationChannelId: String? = null
    lateinit var bigTextStyle: NotificationCompat.BigTextStyle
    lateinit var notificationCompatBuilder: NotificationCompat.Builder

    companion object {
        val STATUS_DOWNLOADING = 1
        val STATUS_FINISHED = 0
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
        mNotificationManagerCompat = NotificationManagerCompat.from(baseContext)
        notificationChannelId = Utils.createNotificationChannel(baseContext)
        bigTextStyle = NotificationCompat.BigTextStyle().bigText("big text").setBigContentTitle("big content title")
        notificationCompatBuilder = NotificationCompat.Builder(
            applicationContext, notificationChannelId!!
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
        val receiverIntent = Intent(baseContext, PlayerActivity.DownloadReceiver::class.java)
        receiverIntent.action = "downlaod"
        val videoItem = intent!!.getParcelableExtra<VideoItem>(BundleKeys.VIDEO)
        Thread().run {
            var downloadId = 800

            downloadId = PRDownloader.download(
                Url.BASE_URL + videoItem?.name,
                filesDir.path,
                videoItem?.downloadName
            )
                .build()
                .setOnPauseListener {

                }
                .setOnProgressListener {
                    Log.i(TAG, "current bytes"+it.currentBytes +" total bytes = " + it.totalBytes)
                    Log.i(TAG, "downlaod setOnProgressListener "+(it.currentBytes * 100/ it.totalBytes).toInt())

                    updateNotification(downloadId,100,(it.currentBytes * 100/ it.totalBytes).toInt())
                    //                download.setImageResource(R.drawable.ic_download)
                }
                .setOnStartOrResumeListener {
                    receiverIntent.putExtra(BundleKeys.DOWNLOAD_STATUS, STATUS_DOWNLOADING)
                    LocalBroadcastManager.getInstance(baseContext).sendBroadcast(receiverIntent)
                    showNotification(downloadId)
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        Log.i(TAG, "downlaod complete")
//                    download.setImageResource(R.drawable.ic_file_download)
                        receiverIntent.putExtra(BundleKeys.DOWNLOAD_STATUS, STATUS_FINISHED)
                        LocalBroadcastManager.getInstance(baseContext).sendBroadcast(receiverIntent)
                        updateNotification(downloadId,0, 0)
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
        Log.i(TAG, "allDownloadFinished")
        return true
    }

    fun showNotification(notificationId: Int) {
        val areNotificationsEnabled = mNotificationManagerCompat.areNotificationsEnabled()

        if (!areNotificationsEnabled) {
            // Because the user took an action to create a notification, we create a prompt to let
            // the user re-enable notifications for this application again.
//            val snackbar = Snackbar
////                .make(
////                    mMainRelativeLayout,
////                    "You need to enable notifications for this app",
////                    Snackbar.LENGTH_LONG
////                )
////                .setAction("ENABLE", object : View.OnClickListener() {
////                    fun onClick(view: View) {
////                        // Links to this app's notification settings
////                        openNotificationSettingsForApp()
////                    }
////                })
////            snackbar.show()
            return
        }
//        val notificationChannelId = Utils.createNotificationChannel(baseContext)
//
//        val bigTextStyle = NotificationCompat.BigTextStyle().bigText("big text").setBigContentTitle("big content title")
//        val notificationCompatBuilder = NotificationCompat.Builder(
//            applicationContext, notificationChannelId!!
//        )

        val notification = notificationCompatBuilder
            // BIG_TEXT_STYLE sets title and content for API 16 (4.1 and after).
            .setStyle(bigTextStyle)
            // Content for API <24 (7.0 and below) devices.
            .setContentText("big content title")
            .setSmallIcon(com.espica.R.mipmap.ic_launcher)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    com.espica.R.drawable.ic_download
                )
            )
//            .setContentIntent(notifyPendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            // Set primary color (important for Wear 2.0 Notifications).
            .setColor(ContextCompat.getColor(applicationContext, com.espica.R.color.colorPrimary))

            // SIDE NOTE: Auto-bundling is enabled for 4 or more notifications on API 24+ (N+)
            // devices and all Wear devices. If you have more than one notification and
            // you prefer a different summary notification, set a group key and create a
            // summary notification via
            // .setGroupSummary(true)
            // .setGroup(GROUP_KEY_YOUR_NAME_HERE)

//            .setCategory(Notification.CATEGORY_REMINDER)

            // Sets priority for 25 and below. For 26 and above, 'priority' is deprecated for
            // 'importance' which is set in the NotificationChannel. The integers representing
            // 'priority' are different from 'importance', so make sure you don't mix them.
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setProgress(100, 0, false)

            // Sets lock-screen visibility for 25 and below. For 26 and above, lock screen
            // visibility is set in the NotificationChannel.
//            .setVisibility(bigTextStyleReminderAppData.getChannelLockscreenVisibility())

            // Adds additional actions specified above.
//            .addAction(snoozeAction)
//            .addAction(dismissAction)

            .build()

        mNotificationManagerCompat.notify(notificationId, notification)
    }

    fun updateNotification(notificationId: Int, max: Int, progress: Int) {
        val notification = notificationCompatBuilder
            .setProgress(max, progress, false)
            .build()

        mNotificationManagerCompat.notify(notificationId, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }
}
