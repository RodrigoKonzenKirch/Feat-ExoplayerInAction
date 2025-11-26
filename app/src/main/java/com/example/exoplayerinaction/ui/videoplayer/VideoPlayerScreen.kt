package com.example.exoplayerinaction.ui.videoplayer

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayerScreen(viewModel: VideoPlayerViewModel = viewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val player = viewModel.player

    SetPlayerLifecycle(lifecycleOwner, player)

    VideoPlayerUI(player, context)

}

@Composable
fun VideoPlayerUI(player: ExoPlayer, context: Context){
    Column(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f) // Standard video aspect ratio
                .background(Color.Black),

            // Factory function to create the View
            factory = {
                // Create the PlayerView (ExoPlayer's dedicated UI component in media3)
                PlayerView(context).apply {
                    // Attach the player instance from the ViewModel
                    setPlayer(player)
                    // Optional: Set full screen button listener, control visibility, etc.
                    useController = true
                }
            },

            // Update function (optional, but good practice if player state changes outside of factory)
            update = { playerView ->
                // Re-attach the player if it was somehow detached or updated
                playerView.setPlayer(player)
            }
        )

        CustomControlUI(modifier = Modifier, player)

    }
}


@Composable
fun SetPlayerLifecycle(lifecycleOwner: LifecycleOwner, player: ExoPlayer){

    // This effect ensures the player's lifecycle (pause/resume) is tied to the Composable's lifecycle
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                // Pause the player when the app is paused (e.g., backgrounded)
                Lifecycle.Event.ON_PAUSE -> player.pause()
                // Resume playback when the app returns to the foreground
                Lifecycle.Event.ON_RESUME -> player.play()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        // Clean up the observer when the composable leaves the screen
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun CustomControlUI(modifier: Modifier, player: ExoPlayer){

    // Example Control UI (Compose-based) ---
    Column(modifier = modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(8.dp))

        // Example: A simple Compose button to toggle play/pause
        // Custom UI in compose can be implemented here based on the player's state
        Button(
            onClick = {
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.play()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (player.isPlaying) "Pause (Compose Control)" else "Play (Compose Control)")
        }

        val status = if (player.isPlaying) "Playing"
        else if (player.isLoading) "Loading"
        else if (player.isPlayingAd) "Playing Ads"
        else if (!player.isPlaying) "Paused"
        else "Unknown Status"
        val videoFormat = player.videoFormat?.containerMimeType
        val videoHeight = player.videoFormat?.height
        val videoWidth = player.videoFormat?.width

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Video Playback Status: $status",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Video Height: $videoHeight")
        Text(text = "Video Width: $videoWidth")
        Text(text = "Video Format: $videoFormat")

        // Use a state to display player status (e.g., current position)
        var playbackPosition by remember { mutableLongStateOf(0L) }

        // Simple mechanism to update the UI with the current position every 500ms
        LaunchedEffect(Unit) {
            while (true) {
                kotlinx.coroutines.delay(500)
                playbackPosition = player.currentPosition
            }
        }

        Text("Current Position: ${playbackPosition / 1000} seconds")
    }
}