package com.espica

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import android.net.Uri
import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.fragment.app.FragmentActivity
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.ui.SubtitleView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.source.SingleSampleMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.Format.NO_VALUE
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.text.Cue
import com.google.android.exoplayer2.text.TextRenderer
import com.google.android.exoplayer2.text.webvtt.WebvttCue
import kotlinx.android.synthetic.main.activity_player.view.*
import java.util.ArrayList


class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
    }

    override fun onStart() {
        super.onStart()
        initializePlayer();
    }

    private fun initializePlayer() {
//        // Create a default TrackSelector
//        val bandwidthMeter = DefaultBandwidthMeter()
//        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
//        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
//
//        //Initialize the player
//        var player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
//
//        //Initialize simpleExoPlayerView
//        val simpleExoPlayerView = findViewById<SimpleExoPlayerView>(R.id.exoplayer)
//        simpleExoPlayerView.setPlayer(player)
//
//        // Produces DataSource instances through which media data is loaded.
//        val dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "CloudinaryExoplayer"))
//
//        // Produces Extractor instances for parsing the media data.
//        val extractorsFactory = DefaultExtractorsFactory()
//
//        // This is the MediaSource representing the media to be played.
//        val videoUri = Uri.parse("assets:///movie.mp4")
//        val videoSource = ExtractorMediaSource(
//            videoUri,
//            dataSourceFactory, extractorsFactory, null, null
//        )
//
//        // Prepare the player with the source.
//        player.prepare(videoSource)
//        player.playWhenReady =true
//       val mediaDataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, null))

        val videoFileUri = Uri.parse("asset:///movie.mp4")
        val dataSourceFactory: DataSource.Factory = DataSource.Factory {
            AssetDataSource(
                this@PlayerActivity
            )
        }
        val videoSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(videoFileUri)
        val exoPlayer = ExoPlayerFactory.newSimpleInstance(
            Activity@ this,
            DefaultTrackSelector()
        )
        val simpleExoPlayerView = findViewById<SimpleExoPlayerView>(R.id.exoplayer)
        val webView = findViewById<WebView>(R.id.webView)
        simpleExoPlayerView.setPlayer(exoPlayer)
        simpleExoPlayerView.subtitleView.setVisibility(View.GONE);
        exoPlayer.playWhenReady = true

        val subtitleView = findViewById<SubtitleView>(R.id.subtitle)

        val cueList = ArrayList<Cue>()

        exoPlayer.addTextOutput(object : TextRenderer.Output
        {
            override fun onCues(cues: MutableList<Cue>?) {
                if(subtitleView!=null){
                    subtitleView.onCues(cues)
                    if(cues!=null && cues.size>0) {
                        Log.e("fahi",  cues.last().positionAnchor.toString())
                        Log.e("fahi",  cues.last().position.toString())
                        Log.e("fahi",  cues.last().line.toString())
                        Log.e("fahi",  cues.last().lineAnchor.toString())
                        Log.e("fahi",  cues.last().text.toString())

//                        val cue = cues.last()
//                        cueList.add(cue)
//                        subtitleView.setCues(cueList)
                    }
                }
            }
        })
        webView.loadUrl("file:///android_asset/movie.vtt")
        val textFormat = Format.createTextSampleFormat(
            null, MimeTypes.APPLICATION_SUBRIP,null,
            Format.NO_VALUE, C.SELECTION_FLAG_DEFAULT,"en",null,  Format.OFFSET_SAMPLE_RELATIVE
        )
        val subtitleSource = SingleSampleMediaSource(Uri.parse("asset:///movie.vtt"), dataSourceFactory, textFormat, C.TIME_UNSET)

        val mergedSource = MergingMediaSource(videoSource, subtitleSource)
        exoPlayer.prepare(mergedSource)

    }
}
