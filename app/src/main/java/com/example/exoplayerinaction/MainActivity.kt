package com.example.exoplayerinaction

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.exoplayerinaction.ui.theme.ExoplayerInActionTheme
import com.example.exoplayerinaction.ui.videoplayer.VideoPlayerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExoplayerInActionTheme {
                VideoPlayerScreen()
            }
        }
    }
}

