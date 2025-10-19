package com.kr.expenserecoder

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class AudioService : Service() {

    private lateinit var player: ExoPlayer

    override fun onCreate() {
        super.onCreate()

        // Create ExoPlayer
        player = ExoPlayer.Builder(this).build()

        // Create notification channel (required for Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "audio_channel",
                "Audio Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

//         Build notification
        val notification: Notification = NotificationCompat.Builder(this, "audio_channel")
            .setContentTitle("Playing sound")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

//         Start service in foreground
        startForeground(1, notification)
        
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val resId = intent?.getIntExtra("resId", -1) ?: -1
        if (resId != -1) {
            val rawUri = Uri.parse("android.resource://${packageName}/$resId")
            val mediaItem = MediaItem.fromUri(rawUri)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
