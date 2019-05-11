package com.espica.ui.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.source.ExtractorMediaSource
import android.net.Uri
import android.util.Log
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


class PlayerActivity : AppCompatActivity() {

    var exoPlayer :SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        exoPlayer = ExoPlayerFactory.newSimpleInstance(
            Activity@ this,
            DefaultTrackSelector()
        )
        initializePlayer()
        initializeUi()

    }

    private fun initializeUi() {
        more.setOnClickListener {
            val playerBottomSheet = PlayerBottomSheet()
            playerBottomSheet.show(supportFragmentManager,null)
        }
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
        val videoFileUri = Uri.parse("asset:///movie.mp4")
        val dataSourceFactory: DataSource.Factory = DataSource.Factory {
            AssetDataSource(
                this@PlayerActivity
            )
        }
        val videoSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(videoFileUri)

        val simpleExoPlayerView = findViewById<SimpleExoPlayerView>(R.id.exoplayer)
        val webView = findViewById<WebView>(R.id.webView)
        simpleExoPlayerView.setPlayer(exoPlayer)
        simpleExoPlayerView.subtitleView.setVisibility(View.GONE);
        exoPlayer!!.playWhenReady = true

        val subtitleView = findViewById<SubtitleView>(R.id.subtitle)


        exoPlayer!!.addTextOutput(object : TextRenderer.Output
        {
            override fun onCues(cues: MutableList<Cue>?) {
                if(subtitleView!=null){
                    if(cues!=null && cues.size>0) {
                        Log.e("fahi",  cues.last().positionAnchor.toString())
                        Log.e("fahi",  cues.last().position.toString())
                        Log.e("fahi",  cues.last().line.toString())
                        Log.e("fahi",  cues.last().lineAnchor.toString())
                        Log.e("fahi",  cues.last().text.toString())

                        subtitleView.setCues(cues)
                    }
                }
            }
        })
        webView.loadUrl("file:///android_asset/movie.vtt")
//        val textFormat = Format.createTextSampleFormat(
//            null, MimeTypes.APPLICATION_SUBRIP,null,
//            Format.NO_VALUE, C.SELECTION_FLAG_DEFAULT,"en",null,  Format.OFFSET_SAMPLE_RELATIVE
//        )
        val textFormat = Format.createTextSampleFormat(
            null, MimeTypes.TEXT_VTT,null,
            Format.NO_VALUE, Format.NO_VALUE,"en",null,  Format.OFFSET_SAMPLE_RELATIVE
        )

        val subtitleSource = SingleSampleMediaSource(Uri.parse("asset:///movie.vtt"), dataSourceFactory, textFormat, C.TIME_UNSET)

        val mergedSource = MergingMediaSource(videoSource, subtitleSource)
        exoPlayer!!.prepare(mergedSource)

    }
}
