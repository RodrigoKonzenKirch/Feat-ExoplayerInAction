package com.example.exoplayerinaction.ui.videoplayer

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

/**
 * ViewModel responsible for initializing, managing, and releasing the ExoPlayer instance.
 * Using AndroidViewModel to access the application context for player initialization.
 */
class VideoPlayerViewModel(application: Application) : AndroidViewModel(application) {

    // A sample video URL. Replace this with your actual video source.
    private val videoUri: Uri = Uri.parse("https://storage.googleapis.com/exoplayer-test-media-01/mkv/android-screensavers-540p.mkv")

    // The ExoPlayer instance is exposed publicly but its state should primarily be managed
    // within the ViewModel lifecycle methods.
    val player: ExoPlayer = ExoPlayer.Builder(application)
        .build().apply {
            // Build the MediaItem
            val mediaItem = MediaItem.Builder()
                .setUri(videoUri)
                .build()

            // Set the media item and prepare the player
            setMediaItem(mediaItem)
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