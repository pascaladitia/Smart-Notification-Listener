package com.pascal.smart_notification_listener.utils

import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import com.pascal.smart_notification_listener.service.SmartNotifListenerService

object NotificationPermissionUtils {

    private const val ENABLED_NOTIFICATION_LISTENERS =
        "enabled_notification_listeners"

    fun isGranted(context: Context): Boolean {
        val enabled = Settings.Secure.getString(
            context.contentResolver,
            ENABLED_NOTIFICATION_LISTENERS
        ) ?: return false

        val component = ComponentName(
            context,
            SmartNotifListenerService::class.java
        )

        return enabled
            .split(":")
            .any { it == component.flattenToString() }
    }
}
