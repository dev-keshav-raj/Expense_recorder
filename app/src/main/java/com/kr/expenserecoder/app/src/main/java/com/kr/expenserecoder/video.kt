package com.kr.expenserecoder

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoBackgroundBox(videoResId: Int, content: @Composable BoxScope.() -> Unit) {
    val context = LocalContext.current

    // Initialize ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val videoUri = Uri.parse("android.resource://${context.packageName}/$videoResId")
            val mediaItem = MediaItem.fromUri(videoUri)
            setMediaItem(mediaItem)
            volume = 0f // mute audio
            repeatMode = ExoPlayer.REPEAT_MODE_ONE // loop forever
            prepare()
            playWhenReady = true
        }
    }

    Box() {
        // Video background
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    useController = false // hide controls
                }
            },
//            modifier = Modifier.fillMaxSize()
        )
        content()
    }
}
