package com.github.takahirom.debug.alter.sample

import android.app.NotificationChannel
import android.preference.PreferenceManager
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat

import com.github.takahirom.library.debug.alter.DebugAlter
import com.github.takahirom.library.debug.alter.DebugAlterItem
import android.app.NotificationChannel.DEFAULT_CHANNEL_ID
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.os.BuildCompat


class DebugApp : App() {

    override fun onCreate() {
        super.onCreate()

        val preference = PreferenceManager.getDefaultSharedPreferences(this)
        val items = arrayListOf<DebugAlterItem<*>>(
                object : DebugAlterItem<String>("getSnackbarText") {
                    override fun isAlter(): Boolean = preference.contains(key)
                    override fun get(): String? = preference.getString(key, null)
                },
                object : DebugAlterItem<Boolean>("isSnackbarShowTiming") {
                    override fun isAlter(): Boolean = preference.contains(key)
                    override fun get(): Boolean? = preference.getBoolean(key, false)
                },
                object: DebugAlterItem<String>("helloworld"){
                    override fun isAlter(): Boolean = preference.contains(key)
                    override fun get(): String = preference.getString(key, "")
                }
        )
        DebugAlter.getInstance().setItems(items)

        showNotification()
    }

    private fun showNotification() {
        val notification = getNotificationBuilder(this)
                .setContentTitle("Open debug menu")
                .setContentIntent(PendingIntent.getActivity(this,
                        DEBUG_CHANNEL_ID.hashCode(),
                        DebugMenuActivity.createIntent(this),
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.drawable.ic_launcher_foreground).build()

        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(DEBUG_CHANNEL_ID.hashCode(), notification)
    }

    fun getNotificationBuilder(context: Context): NotificationCompat.Builder {
        if (BuildCompat.isAtLeastO()) {
            createDefaultNotificationChannel(context)
        }
        val builder = NotificationCompat.Builder(context)
        builder.setChannelId(DEBUG_CHANNEL_ID)
        return builder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDefaultNotificationChannel(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val appName = context.getString(R.string.app_name)
        val channel = NotificationChannel(DEBUG_CHANNEL_ID,
                appName, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val DEBUG_CHANNEL_ID = "debug_channel"
    }
}
