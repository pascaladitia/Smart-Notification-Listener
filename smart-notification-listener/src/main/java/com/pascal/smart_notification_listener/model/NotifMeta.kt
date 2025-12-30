package com.pascal.smart_notification_listener.model

import android.graphics.Bitmap

data class NotifMeta(
    val id: Int,
    val packageName: String,
    val appName: String,
    val title: String?,
    val text: String?,
    val postedAt: Long,
    val messages: List<MessagePart>,
    val avatar: Bitmap?,
    val isActive: Boolean,
    val notifType: NotifType
) {

    data class MessagePart(
        val sender: String?,
        val text: String?,
        val time: Long,
        val avatar: Bitmap?
    )
}
