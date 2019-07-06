package com.espica.ui.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.source.ExtractorMediaSource
import android.net.Uri
import android.util.Log
import android.view.ActionMode
import android.view.View
import android.webkit.WebView
import com.espica.R
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SubtitleView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.source.SingleSampleMediaSource
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.text.Cue
import com.google.android.exoplayer2.text.TextRenderer
import kotlinx.android.synthetic.main.exo_playback_control_view.*
import android.view.MenuItem
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.widget.Toast
import com.espica.EspicaApp
import com.espica.data.BundleKeys
import com.espica.data.network.ApiClient
import com.espica.data.network.MyDisposableObserver
import com.espica.data.network.Url
import com.espica.data.network.response.VideoItem
import com.espica.ui.leitner.AddToLeitnerDialog
import com.google.android.exoplayer2.text.TextOutput
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_player.*
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.math.log


class PlayerActivity : AppCompatActivity() {

    private var exoPlayer: SimpleExoPlayer? = null
    private var mActionMode: ActionMode? = null
    private var sentenceCounter = -1
    private var videoItem: VideoItem? = null
    private var srtContent:String?=null

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
        compositeDisposable.add(apiClient.readSrt(videoItem!!.id).subscribeWith(object : MyDisposableObserver<ResponseBody>(){
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
        webView.evaluateJavascript("(function getText(){return window.getSelection().toString()})()",
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

        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSourceFactory("userAgent")
        val videoSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(Url.BASE_URL + videoItem?.name))

        simpleExoplayer.setPlayer(exoPlayer)
        simpleExoplayer.subtitleView.setVisibility(View.GONE);
        exoPlayer!!.playWhenReady = true

        exoPlayer!!.addTextOutput(object : TextOutput {
            override fun onCues(cues: MutableList<Cue>?) {
                if (subtitle != null) {
                    if (cues != null && cues.size > 0) {
                        Log.e("fahi position", cues.last().position.toString())
                        Log.e("fahi position anchor", cues.last().toString())

                        Log.e("fahi", cues.last().text.toString())
                        var jsText = ""
                        if (sentenceCounter > -1)
                            jsText = "document.body.children[" + sentenceCounter + "].style = '';"
                        sentenceCounter++
                        jsText += "var node = document.body.children[" + sentenceCounter + "];" +
                                "node.style.fontWeight = 'bold' ;" +
                                "window.scrollTo(node.offsetLeft,node.offsetTop);"

                        webView.evaluateJavascript(
                            jsText,
                            object : ValueCallback<String> {
                                override fun onReceiveValue(text: String?) {
                                    Log.e("fahiiiii", text)
                                }
                            }
                        )

//                        subtitle.setCues(cues)
                    }
                }
            }
        })

//        webView.loadUrl("file:///android_asset/6_tags.html")
        webView.loadUrl(Url.BASE_URL+"api/html/download/?video_id="+videoItem?.id);
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(WebAppInterface(), "android")

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
            SingleSampleMediaSource(Uri.parse(Url.BASE_URL+"api/srt/download/" + "?video_id=" + videoItem?.id), dataSourceFactory, textFormat, 5 * 60 * 1000)

        val mergedSource = MergingMediaSource(videoSource, subtitleSource)
        exoPlayer!!.prepare(mergedSource)


    }

    open class WebAppInterface {
        @JavascriptInterface
        fun seekTo(number: Int) {

        }
    }
}
