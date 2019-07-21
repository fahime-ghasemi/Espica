package com.espica.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
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
import androidx.core.content.ContextCompat
import android.app.PendingIntent


class DownloadService : Service() {

    val TAG = "DownloadService"
    val downlodList = ArrayList<Int>()
    lateinit var mNotificationManagerCompat: NotificationManagerCompat
    var notificationChannelId: String? = null
    lateinit var bigTextStyle: NotificationCompat.BigTextStyle
    lateinit var notificationCompatBuilder: NotificationCompat.Builder
    var videoItem: VideoItem? = null

    companion object {
        val STATUS_DOWNLOADING = 1
        val STATUS_CANCEL = 2
        val STATUS_FINISHED = 0
        val REQUEST_CODE_CANCEL = 206
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
        mNotificationManagerCompat = NotificationManagerCompat.from(baseContext)
        notificationChannelId = Utils.createNotificationChannel(baseContext)
        bigTextStyle = NotificationCompat.BigTextStyle()
        notificationCompatBuilder = NotificationCompat.Builder(
            applicationContext, notificationChannelId!!
        )
        notificationCompatBuilder
            // BIG_TEXT_STYLE sets title and content for API 16 (4.1 and after).
            .setStyle(bigTextStyle)
            // Content for API <24 (7.0 and below) devices.
            .setSmallIcon(com.espica.R.drawable.ic_notification_download)
//            .setLargeIcon(
//                BitmapFactory.decodeResource(
//                    resources,
//                    com.espica.R.drawable.ic_download
//                )
//            )
//            .setContentIntent(notifyPendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
//            .setCustomContentView()
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
//            .setcus(0, "Cancel", pendingItem)

        // Sets lock-screen visibility for 25 and below. For 26 and above, lock screen
        // visibility is set in the NotificationChannel.
//            .setVisibility(bigTextStyleReminderAppData.getChannelLockscreenVisibility())


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
        val receiverIntent = Intent(baseContext, PlayerActivity.DownloadReceiver::class.java)
        receiverIntent.action = "downlaod"
        if (intent!!.getBooleanExtra(BundleKeys.DOWNLOAD_CANCEL, false)) {
            val downloadId = intent.getIntExtra(BundleKeys.DOWNLOAD_ID, 0)
            if (downloadId != 0) {
                PRDownloader.cancel(downloadId)
                downlodList.remove(downloadId)
                cancelNotification(downloadId)
                receiverIntent.putExtra(BundleKeys.DOWNLOAD_STATUS, STATUS_CANCEL)
                LocalBroadcastManager.getInstance(baseContext).sendBroadcast(receiverIntent)
                if (allDownloadFinished()) stopSelf()
                return START_NOT_STICKY
            }
        }

        videoItem = intent!!.getParcelableExtra<VideoItem>(BundleKeys.VIDEO)
        bigTextStyle.setBigContentTitle(videoItem?.title)
        Thread().run {
            var downloadId = 0
            downloadId = PRDownloader.download(
                Url.BASE_URL + videoItem?.name,
                filesDir.path,
                videoItem?.downloadName
            )
                .build()
                .setOnPauseListener {

                }
                .setOnProgressListener {
                    updateNotification(downloadId, 100, (it.currentBytes * 100 / it.totalBytes).toInt())
                }
                .setOnStartOrResumeListener {
                    receiverIntent.putExtra(BundleKeys.DOWNLOAD_STATUS, STATUS_DOWNLOADING)
                    LocalBroadcastManager.getInstance(baseContext).sendBroadcast(receiverIntent)
                    showNotification(downloadId)
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
//                    download.setImageResource(R.drawable.ic_file_download)
                        receiverIntent.putExtra(BundleKeys.DOWNLOAD_STATUS, STATUS_FINISHED)
                        LocalBroadcastManager.getInstance(baseContext).sendBroadcast(receiverIntent)
                        updateNotification(downloadId, 0, 0)
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
        val cancelIntent = Intent(baseContext, DownloadService::class.java)
        cancelIntent.putExtra(BundleKeys.DOWNLOAD_CANCEL, true)
        cancelIntent.putExtra(BundleKeys.DOWNLOAD_ID, notificationId)
        val pendingIntent =
            PendingIntent.getService(baseContext, REQUEST_CODE_CANCEL, cancelIntent, PendingIntent.FLAG_CANCEL_CURRENT)

//        val collapseRemoteView = RemoteViews(baseco)
        if (notificationCompatBuilder.mActions.size > 0)
            notificationCompatBuilder.mActions.removeAt(0)
        val notification = notificationCompatBuilder
            // BIG_TEXT_STYLE sets title and content for API 16 (4.1 and after).
            // Content for API <24 (7.0 and below) devices.
            .setContentText(videoItem?.title)
            .addAction(0, "Cancel", pendingIntent)
            .build()

        notificationCompatBuilder.setStyle(bigTextStyle)
        mNotificationManagerCompat.notify(notificationId, notification)
    }

    fun cancelNotification(notificationId: Int) {
        mNotificationManagerCompat.cancel(notificationId)
    }

    fun updateNotification(notificationId: Int, max: Int, progress: Int) {
        if (progress % 5 != 0) return
        if (max == 0 && progress == 0) {
            if (notificationCompatBuilder.mActions.size > 0)
                notificationCompatBuilder.mActions.removeAt(0)
            bigTextStyle.bigText("Download Complete")

        }
        val notification = notificationCompatBuilder
            .setProgress(max, progress, false)
            .setStyle(bigTextStyle)
            .build()

        mNotificationManagerCompat.notify(notificationId, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }
}
