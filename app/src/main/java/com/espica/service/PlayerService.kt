package com.espica.service

import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media.session.MediaButtonReceiver
import com.espica.data.network.Url
import com.espica.tools.NotificationManager
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.text.Cue
import com.google.android.exoplayer2.text.TextOutput
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes

/**
 * Created by fahime on 8/19/17.
 */

class PlayerService : Service(), Player.EventListener {
    private val mBinder = MyBinder()
    var exoPlayer: SimpleExoPlayer? = null
    private var mMediaSession: MediaSessionCompat? = null
    private var mStateBuilder: PlaybackStateCompat.Builder? = null
    private var isBounded: Boolean = false
    var mediaUri: Uri? = null
    lateinit var mediaTitle: String
    lateinit var notificationManager : NotificationManager


    override fun onCreate() {
        super.onCreate()
        Log.i(LOG_TAG, "onCreate")
        // Initialize the Media Session.
        notificationManager = NotificationManager(baseContext)
        initializeMediaSession()
        initializePlayer()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i(LOG_TAG, "onStartCommand")
        MediaButtonReceiver.handleIntent(mMediaSession, intent)
        return Service.START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.i(LOG_TAG, "onBind")
        isBounded = true
        return mBinder
    }

    override fun onRebind(intent: Intent) {
        super.onRebind(intent)
        isBounded = true
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.i(LOG_TAG, "onUnbind")
        isBounded = false
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(LOG_TAG, "onDestroy")
        releasePlayer()
        mMediaSession!!.isActive = false
    }

    inner class MyBinder : Binder() {
        val service: PlayerService
            get() = this@PlayerService

    }

    private fun initializePlayer() {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayerFactory.newSimpleInstance(
                this,
                DefaultTrackSelector()
            )
            // Create an instance of the ExoPlayer.
            //            TrackSelector trackSelector = new DefaultTrackSelector();
            //            LoadControl loadControl = new DefaultLoadControl();
            //            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            exoPlayer!!.addListener(this)
        }
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private fun initializeMediaSession() {
        val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
        mediaButtonIntent.setClass(this, MediaButtonReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0)
        // Create a MediaSessionCompat.
        val mediaButtonReceiver = ComponentName(applicationContext, MediaButtonReceiver::class.java)

        mMediaSession = MediaSessionCompat(this, LOG_TAG, mediaButtonReceiver, pendingIntent)

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession!!.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )

        // Do not let MediaButtons restart the player when the app is not visible.

        //        mMediaSession.setMediaButtonReceiver(pendingIntent);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                        PlaybackStateCompat.ACTION_STOP or
                        PlaybackStateCompat.ACTION_FAST_FORWARD or
                        PlaybackStateCompat.ACTION_REWIND or
                        PlaybackStateCompat.ACTION_PLAY_PAUSE
            )

        mMediaSession!!.setPlaybackState(mStateBuilder!!.build())

        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession!!.setCallback(MySessionCallback())

        // Start the Media Session since the activity is active.
        mMediaSession!!.isActive = true
    }

    /**
     * Release ExoPlayer.
     */
    private fun releasePlayer() {
        //        stopForeground(true);
        exoPlayer!!.stop()
        exoPlayer!!.release()
        exoPlayer = null
    }

    fun preparePlayer(isFileDownloaded: Boolean, videoId: Int) {
        // Prepare the MediaSource.
        if (this.mediaUri == null) return

        notificationManager.initialPlayingNotification(mediaTitle)
        val factory: DataSource.Factory
        if (isFileDownloaded) {
            factory = DefaultDataSourceFactory(this, "userAgent")
        } else
            factory = DefaultHttpDataSourceFactory("userAgent")


        //        String userAgent = Util.getUserAgent(this, "ClassicalMusicQuiz");
        //        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
        //                this, userAgent), new DefaultExtractorsFactory(), null, null);
        //        mExoPlayer.prepare(mediaSource);
        //        mExoPlayer.setPlayWhenReady(true);

        val videoSource = ExtractorMediaSource.Factory(factory).createMediaSource(mediaUri)
        val textFormat = Format.createTextSampleFormat(
            null, MimeTypes.APPLICATION_SUBRIP, null,
            Format.NO_VALUE, Format.NO_VALUE, "en", null, Format.OFFSET_SAMPLE_RELATIVE
        )
        val subtitleSource = SingleSampleMediaSource.Factory(factory).createMediaSource(
            Uri.parse(Url.BASE_URL + "api/srt/download/" + "?video_id=" + videoId),
            textFormat, (4 * 60 * 1000).toLong()
        )

        val mergedSource = MergingMediaSource(videoSource, subtitleSource)
        exoPlayer!!.playWhenReady = true
        exoPlayer!!.prepare(mergedSource)
    }


    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {

    }

    override fun onLoadingChanged(isLoading: Boolean) {

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_READY && playWhenReady) {
            Log.i(LOG_TAG, "onPlayerStateChanged palying")

            mStateBuilder!!.setState(
                PlaybackStateCompat.STATE_PLAYING,
                exoPlayer!!.currentPosition, 1f
            )
        } else if (playbackState == Player.STATE_READY) {
            mStateBuilder!!.setState(
                PlaybackStateCompat.STATE_PAUSED,
                exoPlayer!!.currentPosition, 1f
            )
            Log.i(LOG_TAG, "onPlayerStateChanged pause")

        } else if (playbackState == Player.STATE_ENDED) {
            mStateBuilder!!.setState(PlaybackStateCompat.STATE_STOPPED, exoPlayer!!.currentPosition, 1f)
        }
        mMediaSession!!.setPlaybackState(mStateBuilder!!.build())
        notificationManager.showPlayingNotification(mMediaSession!!.sessionToken, mStateBuilder!!.build(),10)
    }

    override fun onRepeatModeChanged(repeatMode: Int) {

    }

    override fun onPlayerError(error: ExoPlaybackException?) {

    }


    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {

    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private inner class MySessionCallback : MediaSessionCompat.Callback() {
        override fun onPlay() {
            Log.i(LOG_TAG, "MySessionCallback Play")
            if (exoPlayer!!.playbackState == Player.STATE_ENDED)
                exoPlayer!!.seekTo(0)
            exoPlayer!!.playWhenReady = true
        }

        override fun onPause() {
            Log.i(LOG_TAG, "MySessionCallback Pause")
            exoPlayer!!.playWhenReady = false
        }

        override fun onSkipToNext() {
            super.onSkipToNext()
            Log.i(LOG_TAG, "MySessionCallback skip to next")
        }

        override fun onStop() {
            super.onStop()
            if (!isBounded)
                stopSelf()
            Log.i(LOG_TAG, "MySessionCallback stop")
        }

        override fun onSkipToPrevious() {
            exoPlayer!!.seekTo(0)
        }
    }

    companion object {

        private val LOG_TAG = PlayerService::class.java.simpleName
    }
}
