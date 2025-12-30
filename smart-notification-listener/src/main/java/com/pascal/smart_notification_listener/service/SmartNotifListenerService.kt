package com.pascal.smart_notification_listener.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.pascal.smart_notification_listener.core.NotifRepository
import com.pascal.smart_notification_listener.core.toNotifMeta

class SmartNotifListenerService : NotificationListenerService() {

    override fun onListenerConnected() {
        activeNotifications
            ?.mapNotNull { it.toNotifMeta(this) }
            ?.let { NotifRepository.setAll(it) }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn?.toNotifMeta(this)?.let {
            NotifRepository.addOrUpdate(it)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        sbn?.let {
            NotifRepository.remove(it.id, it.packageName)
        }
    }
}
