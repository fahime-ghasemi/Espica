package com.espica.tools

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.io.File
import com.google.android.exoplayer2.util.NotificationUtil.createNotificationChannel
import android.app.NotificationManager
import android.app.NotificationChannel
import androidx.core.app.NotificationCompat.getChannelId
import android.os.Build
import com.google.android.exoplayer2.util.NotificationUtil


class Utils {
    companion object {
        fun convertDpToPx(context: Context, dp: Float): Float {
            return dp * context.getResources().getDisplayMetrics().density
        }

        fun isFileDownloaded(filePath: String): Boolean {
            val file = File(filePath)
            return file.exists()
        }

        fun createNotificationChannel(
            context: Context
        ): String{

            // NotificationChannels are required for Notifications on O (API 26) and above.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                // The id of the channel.
                val channelId = "espica"

                // The user-visible name of the channel.
                val channelName = "Espica channel"
                // The user-visible description of the channel.
                val channelDescription = "Espica channel desc"
                val channelImportance = NotificationManager.IMPORTANCE_DEFAULT
                val channelEnableVibrate =false
//                val channelLockscreenVisibility = false

                // Initializes NotificationChannel.
                val notificationChannel = NotificationChannel(channelId, channelName, channelImportance)
                notificationChannel.description = channelDescription
                notificationChannel.enableVibration(channelEnableVibrate)
//                notificationChannel.lockscreenVisibility = channelLockscreenVisibility

                // Adds NotificationChannel to system. Attempting to create an existing notification
                // channel with its original values performs no operation, so it's safe to perform the
                // below sequence.
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(notificationChannel)

                return channelId
            } else {
                // Returns null for pre-O (26) devices.
                return ""
            }
        }
    }

    fun hideKeyboard(activity: Activity) {
        // Check if no leitnerView has focus:
        val view = activity.currentFocus
        if (view != null) {
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
            if (view is EditText) {
                view.clearFocus()
            }

            activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
        // dismiss keyboard for sumsung keyboard
        activity.window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }

    fun showKeyboard(activity: Activity, editView: View) {
        // Check if no leitnerView has focus:
        val view = activity.currentFocus
        if (view != null) {
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(editView, InputMethodManager.SHOW_IMPLICIT)
        }
    }



}