package com.kr.expenserecoder

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest


@OptIn(UnstableApi::class)
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
//            repeatMode = ExoPlayer.REPEAT_MODE_ONE // loop forever
            prepare()
            playWhenReady = true
        }
    }

    Box() {
        // Video background
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    useController = false // hide controls
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            },
        )
        content()
    }
}

@Composable
fun VideoPlayer(videoResId: Int) {
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

    // Display the video
    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = false // hide controls
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
fun SmoothGifPlayer(gifResId: Int ) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(GifDecoder.Factory())
        }
        .build()

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(gifResId)
            .build(),
        contentDescription = "Animated GIF",
        imageLoader = imageLoader,
        //contentScale = ContentScale.Fit, // scale without stretching
        modifier = Modifier.fillMaxSize(),
        //filterQuality = FilterQuality.High // smooth scaling
    )
}