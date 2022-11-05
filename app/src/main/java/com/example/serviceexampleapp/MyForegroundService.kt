package com.example.serviceexampleapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MyForegroundService() : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread(Runnable {
            while (true) {
                Log.e("Debugging!!", "Foreground service is running!")
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start()

        var notification = createNotification()

        startForeground(1001, notification) // we have to give notification so that user
        // knows something is running in the back

        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotification(): Notification {
        val channelID = "Foreground service channel ID"
        val notificationTitle = "Service running!"
        val notificationText = "Foreground service is running!"

        var builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //Before you can deliver the
            // notification on Android 8.0 and higher, you must register your app's notification
            // channel with the system by passing an instance of NotificationChannel to
            // createNotificationChannel(). Above Notification and this channel is connected by same "channel id"
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        return builder.build()
    }
}