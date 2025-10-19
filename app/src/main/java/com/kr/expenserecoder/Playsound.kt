package com.kr.expenserecoder

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

fun playAudioFromRaw(context: Context, player: ExoPlayer, resourceId: Int) {
    // Create URI for the raw resource
    val rawUri = Uri.parse("android.resource://${context.packageName}/$resourceId")

    // Create MediaItem for ExoPlayer
    val mediaItem = MediaItem.fromUri(rawUri)

    // Set and play
    player.setMediaItem(mediaItem)
    player.prepare()
    player.play()
}
