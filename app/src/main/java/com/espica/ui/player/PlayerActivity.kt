package com.espica.ui.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.source.ExtractorMediaSource
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.view.ActionMode
import android.view.View
import com.espica.R
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.source.SingleSampleMediaSource
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.text.Cue
import kotlinx.android.synthetic.main.exo_playback_control_view.*
import android.view.MenuItem
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import androidx.annotation.MainThread
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.espica.EspicaApp
import com.espica.data.BundleKeys
import com.espica.data.network.ApiClient
import com.espica.data.network.MyDisposableObserver
import com.espica.data.network.Url
import com.espica.data.network.response.VideoItem
import com.espica.service.DownloadService
import com.espica.tools.Utils
import com.espica.tools.srtparser.SRTInfo
import com.espica.tools.srtparser.SRTReader
import com.espica.ui.leitner.AddToLeitnerDialog
import com.google.android.exoplayer2.text.TextOutput
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_player.*
import okhttp3.ResponseBody
import java.io.File


class PlayerActivity : AppCompatActivity() {

    private var exoPlayer: SimpleExoPlayer? = null
    private var mActionMode: ActionMode? = null
    private var sentenceCounter = -1
    private var sentenceCounterBeforeSeek = -1
    private var videoItem: VideoItem? = null
    private val TAG = "PlayerActivity"
    private val downloadReceiver: DownloadReceiver = DownloadReceiver()
    private var srtInfo: SRTInfo? = null
    private lateinit var tempFile: File
    private lateinit var filePath: String
    private var fileDownloadStatus = VideoItem.NOT_DOWNLOADED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.espica.R.layout.activity_player)
        exoPlayer = ExoPlayerFactory.newSimpleInstance(
            Activity@ this,
            DefaultTrackSelector()
        )
        if (intent.extras?.containsKey(BundleKeys.VIDEO) == true)
            videoItem = intent.extras?.getParcelable(BundleKeys.VIDEO)

        filePath = filesDir.path + File.separator + videoItem?.downloadName
        fileDownloadStatus = if (Utils.isFileDownloaded(filePath)) VideoItem.DOWNLOADED else
            VideoItem.NOT_DOWNLOADED

        readSrtFile()
        initializePlayer()
        initializeUi()

    }

    private fun readSrtFile() {
        val compositeDisposable = CompositeDisposable()
        val apiClient = ApiClient((application as EspicaApp).networkApiService)
        compositeDisposable.add(apiClient.readSrt(videoItem!!.id).subscribeWith(object :
            MyDisposableObserver<ResponseBody>() {
            override fun onSuccess(response: ResponseBody) {
                val srtContent = response.source().readUtf8()
                tempFile = File.createTempFile("id_" + videoItem!!.id.toString(), null, cacheDir)
                tempFile.writeText(srtContent, Charsets.UTF_8)
                srtInfo = SRTReader.read(tempFile)
            }
        }))

    }

    private fun initializeUi() {
        more.setOnClickListener {
            val playerBottomSheet = PlayerBottomSheet()
            playerBottomSheet.show(supportFragmentManager, null)
        }
        if (fileDownloadStatus == VideoItem.DOWNLOADED)
            download.setImageResource(R.drawable.ic_file_downloaded)
        else if (fileDownloadStatus == VideoItem.NOT_DOWNLOADED)
            download.setImageResource(R.drawable.ic_file_download)

        download.setOnClickListener {
            if (fileDownloadStatus == VideoItem.NOT_DOWNLOADED) {
                val intent = Intent(baseContext, DownloadService::class.java)
                intent.putExtra(BundleKeys.VIDEO, videoItem)
                startService(intent)
            } else if (fileDownloadStatus == VideoItem.DOWNLOADING) {

            }

        }
    }

    override fun onActionModeStarted(mode: ActionMode?) {
        if (mActionMode == null) {
            mActionMode = mode
            mode?.menu?.add(R.string.add_to_litner)
                ?.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem?): Boolean {
                        addToLitner()
                        mActionMode?.finish()
                        return true
                    }
                })
        }
        super.onActionModeStarted(mode)
    }


    override fun onActionModeFinished(mode: ActionMode?) {
        mActionMode = null
        super.onActionModeFinished(mode)
    }

    private fun addToLitner() {
        webViewEng.evaluateJavascript("(function getText(){return window.getSelection().toString()})()",
            object : ValueCallback<String> {
                override fun onReceiveValue(text: String?) {
                    if (text!!.length > 3) {
                        val bundle = Bundle()
                        bundle.putString(BundleKeys.TITLE, text.drop(1).dropLast(1))
                        val addToLeitnerDialog = AddToLeitnerDialog.newInstance(bundle)
                        addToLeitnerDialog.show(supportFragmentManager, null)
                    }
                }
            })

    }

    override fun onResume() {
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(downloadReceiver, IntentFilter("downlaod"))

        super.onResume()
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(downloadReceiver)
        super.onPause()
    }

    override fun onDestroy() {
        releasePlayer()
        if(tempFile.exists()) tempFile.delete()
        super.onDestroy()
    }


    private fun releasePlayer() {
        exoPlayer!!.stop()
        exoPlayer!!.release()
        exoPlayer = null
    }

    private fun initializePlayer() {
//        val dataSourceFactoryAsset: DataSource.Factory = DataSource.Factory {
//            AssetDataSource(
//                this@PlayerActivity
//            )
//        }


        val dataSourceFactory: DataSource.Factory = if (fileDownloadStatus == VideoItem.DOWNLOADED) DefaultDataSourceFactory(
            this,
            "userAgent"
        ) else DefaultHttpDataSourceFactory("userAgent")
        val uri = if (fileDownloadStatus == VideoItem.DOWNLOADED)
            Uri.parse(filePath)
        else
            Uri.parse(Url.BASE_URL + videoItem?.name)

        val videoSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)

        simpleExoplayer.setPlayer(exoPlayer)
        simpleExoplayer.subtitleView.setVisibility(View.GONE);
        exoPlayer!!.playWhenReady = true

        exoPlayer!!.addTextOutput(object : TextOutput {
            override fun onCues(cues: MutableList<Cue>?) {
                if (cues != null && cues.size > 0) {
                    var jsText = ""
                    if (sentenceCounter > -1)
                        jsText = "document.body.children[" + sentenceCounter + "].style = '';"

                    if (sentenceCounterBeforeSeek > -1)
                        jsText = "document.body.children[" + sentenceCounterBeforeSeek + "].style = '';"
                    sentenceCounterBeforeSeek = -1

                    sentenceCounter++
                    jsText += "var node = document.body.children[" + sentenceCounter + "];" +
                            "node.style.fontWeight = 'bold' ;" +
                            "window.scrollTo(node.offsetLeft,node.offsetTop);"

                    webViewEng.evaluateJavascript(
                        jsText,
                        object : ValueCallback<String> {
                            override fun onReceiveValue(text: String?) {
                            }
                        }
                    )
                    webViewPer.evaluateJavascript(jsText, null)

                }
            }
        })

//        webViewEng.loadUrl("file:///android_asset/6_tags.html")
        webViewEng.loadUrl(Url.BASE_URL + "api/html/download/?video_id=" + videoItem?.id)
        webViewEng.settings.javaScriptEnabled = true
        webViewEng.addJavascriptInterface(WebAppInterface(), "android")

        webViewPer.loadUrl(Url.BASE_URL + "api/html/persian/download/?video_id=" + videoItem?.id)
        webViewPer.settings.javaScriptEnabled = true
        webViewPer.addJavascriptInterface(WebAppInterface(), "android")


//        webView.evaluateJavascript("(function getText(){return window.getSelection().toString()})()",
//            object : ValueCallback<String> {
//                override fun onReceiveValue(text: String?) {
//                    Toast.makeText(this@PlayerActivity, text, Toast.LENGTH_LONG).show()
//                }
//            })
//        val textFormat = Format.createTextSampleFormat(
//            null, MimeTypes.APPLICATION_SUBRIP,null,
//            Format.NO_VALUE, C.SELECTION_FLAG_DEFAULT,"en",null,  Format.OFFSET_SAMPLE_RELATIVE
//        )
        val textFormat = Format.createTextSampleFormat(
            null, MimeTypes.APPLICATION_SUBRIP, null,
            Format.NO_VALUE, Format.NO_VALUE, "en", null, Format.OFFSET_SAMPLE_RELATIVE
        )

        val subtitleSource =
            SingleSampleMediaSource(
                Uri.parse(Url.BASE_URL + "api/srt/download/" + "?video_id=" + videoItem?.id),
                dataSourceFactory,
                textFormat,
                5 * 60 * 1000
            )


        val mergedSource = MergingMediaSource(videoSource, subtitleSource)
        exoPlayer!!.prepare(mergedSource)


    }

    open inner class WebAppInterface {
        @JavascriptInterface
        fun seekTo(number: Int) {
            if (srtInfo != null) {
                val srt = srtInfo?.get(number)
                val mainHandler = Handler(mainLooper)
                mainHandler.post {
                    exoPlayer!!.seekTo(srt?.startTime!!)
                    sentenceCounterBeforeSeek = sentenceCounter
                    sentenceCounter = srt.number - 2
                }
                Log.i(TAG, "seek to " + srt?.startTime)
            }
        }
    }

    inner class DownloadReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.i(TAG, "onReceive")
            if (intent?.getIntExtra(BundleKeys.DOWNLOAD_STATUS, 0) == DownloadService.STATUS_DOWNLOADING)
                download.setImageResource(R.drawable.ic_file_downloaded)
            else
                download.setImageResource(R.drawable.ic_file_download)

        }

    }
}
