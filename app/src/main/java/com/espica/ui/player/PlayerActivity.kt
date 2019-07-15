package com.espica.ui.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.source.ExtractorMediaSource
import android.net.Uri
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
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.OnPauseListener
import com.downloader.PRDownloader
import com.espica.EspicaApp
import com.espica.data.BundleKeys
import com.espica.data.network.ApiClient
import com.espica.data.network.MyDisposableObserver
import com.espica.data.network.Url
import com.espica.data.network.response.VideoItem
import com.espica.service.DownloadService
import com.espica.tools.Utils
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
    private var videoItem: VideoItem? = null
    private var srtContent: String? = null
    private val TAG = "PlayerActivity"
    private val downloadReceiver:DownloadReceiver = DownloadReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.espica.R.layout.activity_player)
        exoPlayer = ExoPlayerFactory.newSimpleInstance(
            Activity@ this,
            DefaultTrackSelector()
        )
        if (intent.extras?.containsKey(BundleKeys.VIDEO) == true)
            videoItem = intent.extras?.getParcelable(BundleKeys.VIDEO)

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
                srtContent = response.source().readUtf8()
            }
        }))
    }

    private fun initializeUi() {
        more.setOnClickListener {
            val playerBottomSheet = PlayerBottomSheet()
            playerBottomSheet.show(supportFragmentManager, null)
        }
        download.setOnClickListener {
            val intent = Intent(baseContext, DownloadService::class.java)
            intent.putExtra(BundleKeys.VIDEO, videoItem)
            startService(intent)

//            PRDownloader.download(
//                Url.BASE_URL + videoItem?.name,
//                filesDir.path,
//                videoItem?.downloadName
//            )
//                .build()
//                .setOnPauseListener {
//
//                }
//                .setOnProgressListener {
//                    download.setImageResource(R.drawable.ic_download)
//                }
//                .start(object : OnDownloadListener {
//                    override fun onDownloadComplete() {
//                        Toast.makeText(applicationContext, "downlaod complete", Toast.LENGTH_LONG)
//                        download.setImageResource(R.drawable.ic_file_download)
//                    }
//
//                    override fun onError(error: Error?) {
//                        Toast.makeText(applicationContext, "downlaod error", Toast.LENGTH_LONG)
//
//                    }
//
//                })
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

        val filePath = filesDir.path + File.separator + videoItem?.downloadName
        val isFileDownloaded = Utils.isFileDownloaded(filePath)
        val dataSourceFactory: DataSource.Factory = if (isFileDownloaded) DefaultDataSourceFactory(
            this,
            "userAgent"
        ) else DefaultHttpDataSourceFactory("userAgent")
        val uri = if (isFileDownloaded)
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
                    webViewPer.evaluateJavascript(jsText,null)

                }
            }
        })

//        webView.loadUrl("file:///android_asset/6_tags.html")
        webViewEng.loadUrl(Url.BASE_URL + "api/html/download/?video_id=" + videoItem?.id)
        webViewEng.settings.javaScriptEnabled = true
        webViewEng.addJavascriptInterface(WebAppInterface(), "android")

        webViewPer.loadUrl(Url.BASE_URL + "api/html/persian/download/?video_id=" + videoItem?.id)
        webViewPer.settings.javaScriptEnabled = true

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

    open class WebAppInterface {
        @JavascriptInterface
        fun seekTo(number: Int) {

        }
    }

    inner class DownloadReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.i(TAG, "onReceive")
            if (intent?.getIntExtra(BundleKeys.DOWNLOAD_STATUS, 0) == DownloadService.STATUS_DOWNLOADING)
                download.setImageResource(R.drawable.ic_download)
            else
                download.setImageResource(R.drawable.ic_file_download)

        }

    }
}
