package com.example.exoplayerinaction.ui.videoplayer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

/**
 * ViewModel responsible for initializing, managing, and releasing the ExoPlayer instance.
 * Using AndroidViewModel to access the application context for player initialization.
 */
class VideoPlayerViewModel(application: Application) : AndroidViewModel(application) {

    // A sample playlist of videos URL. Replace this with your actual video source.
    val sampleVideoPlaylistUri = listOf(
        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4",
        "https://cdn.pixabay.com/video/2015/08/20/468-136808389_large.mp4"
    )

    private val mediaItems = mutableListOf<MediaItem>()

    init {
        sampleVideoPlaylistUri.forEach{ videoUri ->
            val mediaItem = MediaItem.Builder()
                .setUri(videoUri)
                .build()
            mediaItems.add(mediaItem)
        }
    }

    // The ExoPlayer instance is exposed publicly but its state should primarily be managed
    // within the ViewModel lifecycle methods.
    val player: ExoPlayer = ExoPlayer.Builder(application)
        .build().apply {
            // Build the MediaItem
            mediaItems.forEach { mediaItem ->
                addMediaItem(mediaItem)
            }
            prepare()

            // Set properties for immediate playback on load
            playWhenReady = true
        }

    /**
     * Called when the ViewModel is no longer used and will be destroyed.
     * Crucially, we release the ExoPlayer instance here to free up resources.
     */
    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}