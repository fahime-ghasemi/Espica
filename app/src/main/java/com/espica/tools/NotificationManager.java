package com.espica.tools;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.media.session.MediaButtonReceiver;
import com.espica.R;
import com.espica.data.BundleKeys;
import com.espica.service.DownloadService;
import com.espica.ui.player.PlayerActivity;
import org.jetbrains.annotations.Nullable;

public class NotificationManager {
    private NotificationManagerCompat notificationManagerCompat;
    private NotificationCompat.Builder downloadBuilder;
    private NotificationCompat.Builder playingBuilder;
    private NotificationCompat.BigTextStyle  bigTextStyle;
    private static final int  REQUEST_CODE_CANCEL = 206;
    private final static String TAG = "NotificationManager";


    private Context context;

    public NotificationManager(Context context) {
        this.context = context;
        notificationManagerCompat = NotificationManagerCompat.from(context);
    }

    public void initialDownloadNotification()
    {
        String notificationChannelId = Utils.Companion.createNotificationChannel(context);
        bigTextStyle = new NotificationCompat.BigTextStyle();
        downloadBuilder = new NotificationCompat.Builder(
                context, notificationChannelId
        );
        downloadBuilder
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
                .setColor(ContextCompat.getColor(context, com.espica.R.color.colorPrimary))

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
                .setProgress(100, 0, false);
    }

    public void initialPlayingNotification(String title)
    {
        String notificationChannelId = Utils.Companion.createNotificationChannel(context);

        playingBuilder = new NotificationCompat.Builder(
                context, notificationChannelId
        );

//        NotificationCompat.Action restartAction = new androidx.core.app.NotificationCompat
//                .Action(R.drawable.exo_controls_previous, context.getString(R.string.restart),
//                MediaButtonReceiver.buildMediaButtonPendingIntent
//                        (getApplicationContext(), PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, 0, new Intent(context, PlayerActivity.class), 0);

        playingBuilder.setContentTitle(title)
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_STOP));

    }

    public void showPlayingNotification(MediaSessionCompat.Token token ,PlaybackStateCompat state,int notificationId)
    {
        Log.i(TAG,"showPlayingNotification");

        int icon;
        String play_pause;
        if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            icon = R.drawable.exo_controls_pause;
            play_pause = context.getString(R.string.pause);
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause = context.getString(R.string.play);
        }

        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));
        playingBuilder.addAction(icon, play_pause,MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                PlaybackStateCompat.ACTION_PLAY_PAUSE) )
          .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(token)
            .setShowActionsInCompactView(0));
        notificationManagerCompat.notify(notificationId, playingBuilder.build());

    }

    public void setDownloadTitle(@Nullable String title) {
        bigTextStyle.setBigContentTitle(title);

    }

    public void showDownloadNotification(int notificationId)
    {
        boolean areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled();

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
            return;
        }

        Intent cancelIntent = new Intent(context, DownloadService.class);
        cancelIntent.putExtra(BundleKeys.Companion.getDOWNLOAD_CANCEL(), true);
        cancelIntent.putExtra(BundleKeys.Companion.getDOWNLOAD_ID(), notificationId);
        PendingIntent pendingIntent =
                PendingIntent.getService(context, REQUEST_CODE_CANCEL, cancelIntent, PendingIntent.FLAG_CANCEL_CURRENT);

//        val collapseRemoteView = RemoteViews(baseco)
        if (downloadBuilder.mActions.size() > 0)
            downloadBuilder.mActions.remove(0);
        downloadBuilder.setStyle(bigTextStyle);
        Notification notification = downloadBuilder
                // BIG_TEXT_STYLE sets title and content for API 16 (4.1 and after).
                // Content for API <24 (7.0 and below) devices.
            .addAction(0, "Cancel", pendingIntent)
            .build();

        notificationManagerCompat.notify(notificationId, notification);
    }

    public void cancelDownloadNotification(int notificationId)
    {
        notificationManagerCompat.cancel(notificationId);
    }

    public void updateDownloadNotification(int notificationId,int progress,int max)
    {
        if (progress % 5 != 0) return;
        if (max == 0 && progress == 0) {
            if (downloadBuilder.mActions.size() > 0)
                downloadBuilder.mActions.remove(0);
            bigTextStyle.bigText("Download Complete");

        }
        Notification notification = downloadBuilder
                .setProgress(max, progress, false)
                .setStyle(bigTextStyle)
                .build();

        notificationManagerCompat.notify(notificationId, notification);
    }
}
