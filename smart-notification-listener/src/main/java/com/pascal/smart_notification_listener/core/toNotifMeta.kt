package com.pascal.smart_notification_listener.core

import android.R.attr.x
import android.app.Notification
import android.content.Context
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import com.pascal.smart_notification_listener.model.NotifMeta
import com.pascal.smart_notification_listener.model.NotifType
import com.pascal.smart_notification_listener.utils.extractSafeAvatar

internal fun StatusBarNotification.toNotifMeta(ctx: Context): NotifMeta? {
    val notification = notification ?: return null
    val extras = notification.extras ?: return null

    val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()
    val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
    val avatarBitmap = extractSafeAvatar(extras, notification, ctx)

    val appName = runCatching {
        val pm = ctx.packageManager
        pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0)).toString()
    }.getOrDefault(packageName)

    return NotifMeta(
        id = id,
        packageName = packageName,
        appName = appName,
        title = title,
        text = text,
        postedAt = postTime,
        messages = emptyList(),
        avatar = avatarBitmap,
        isActive = true,
        notifType = detectNotifType(notification, title, text)
    )
}

private fun detectNotifType(
    n: Notification,
    title: String?,
    text: String?
): NotifType {
    val content = listOfNotNull(title, text).joinToString(" ").lowercase()

    return when {
        n.category == Notification.CATEGORY_CALL && "missed" in content ->
            NotifType.MissedCall

        n.category == Notification.CATEGORY_CALL ->
            NotifType.Call

        "@" in content || "mention" in content ->
            NotifType.Mention

        else -> NotifType.Default
    }
}
