package com.mmt.sampleandroidtvapp

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_main_title.title_content_description
import kotlinx.android.synthetic.main.fragment_main_title.title_content_name
import kotlinx.android.synthetic.main.fragment_main_title_video.*


interface OnMovieInteractedListener {
    fun onMovieInteracted(position: Int, movie: Movie)
}

class MainTitleFragment : Fragment(), OnMovieInteractedListener {

    private lateinit var dataSourceFactory: DefaultDataSourceFactory
    private lateinit var simpleExoPlayer: SimpleExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_main_title_video, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val trackSelector = DefaultTrackSelector()

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
        dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, context?.getString(R.string.app_name))
        )

        simpleExoPlayer.addAnalyticsListener(EventLogger(DefaultTrackSelector()))
        title_content_video_player.player = simpleExoPlayer

    }


    override fun onMovieInteracted(position: Int, movie: Movie) {

        title_content_name.text = movie.title
        title_content_description.text = movie.description

        val uri = Uri.parse(movie.videoUrl)

        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)

        simpleExoPlayer.prepare(videoSource)
        simpleExoPlayer.playWhenReady = true

    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer.release()
    }

}