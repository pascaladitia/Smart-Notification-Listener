package com.pascal.smart_notification_listener.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.pascal.smart_notification_listener.core.NotifRepository
import com.pascal.smart_notification_listener.model.NotifMeta
import kotlinx.coroutines.flow.StateFlow

object SmartNotificationListener {

    val notifications: StateFlow<List<NotifMeta>>
        get() = NotifRepository.notifications

    fun requestPermission(context: Context) {
        context.startActivity(
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}