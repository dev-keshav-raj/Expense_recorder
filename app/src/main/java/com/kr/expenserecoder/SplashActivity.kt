package com.kr.expenserecoder


import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.annotation.RawRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

/**
 * =====================================================================================
 * IMPORTANT SETUP INSTRUCTIONS:
 * 1. Add these dependencies to your app/build.gradle (Module: app) file:
 *
 * dependencies {
 * // Compose UI
 * implementation("androidx.compose.ui:ui-tooling-preview:1.6.8")
 *
 * // Media3 (ExoPlayer) - required for video playback
 * implementation("androidx.media3:media3-exoplayer:1.3.1")
 * implementation("androidx.media3:media3-ui:1.3.1")
 *
 * // Lifecycle KTX
 * implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
 * }
 *
 * 2. Place your video file (e.g., 'splash_video.mp4') in the 'res/raw' directory.
 * =====================================================================================
 *
 * A Composable that displays an MP4 video as a full-screen splash animation.
 *
 * @param videoResId The resource ID of the raw MP4 file (e.g., R.raw.splash_video).
 * @param modifier The modifier to be applied to the PlayerView.
 * @param onVideoFinished Callback executed when the video completes playback.
 */
@OptIn(UnstableApi::class)
@Composable
fun VideoSplashScreen(
    @RawRes videoResId: Int,
    modifier: Modifier = Modifier,
    onVideoFinished: () -> Unit
) {
    val context = LocalContext.current
    var isVideoCompleted by remember { mutableStateOf(false) }

    // Helper to get the video URI from a raw resource ID
    val videoUri = remember(videoResId) {
        getRawResourceUri(context, videoResId)
    }

    // 1. Initialize ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            playWhenReady = true
            prepare()

            // Listener to detect when the video finishes
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        if (!isVideoCompleted) {
                            isVideoCompleted = true
                            onVideoFinished()
                        }
                    }
                }
            })
        }
    }

    // 2. Handle Player Lifecycle
    // This ensures the player releases resources when the composable leaves the screen.
    DisposableEffect(key1 = exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    // 3. Wrap the Android PlayerView using AndroidView
    AndroidView(
        modifier = modifier,
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

                // Hide controls for a typical splash screen experience
                useController = false
            }
        },
        update = {
            // No updates needed typically for a static splash screen
        }
    )
}

private fun getRawResourceUri(context: Context, @RawRes resId: Int): Uri {
    return Uri.Builder()
        .scheme("android.resource")
        .authority(context.packageName)
        .path(resId.toString())
        .build()
}
