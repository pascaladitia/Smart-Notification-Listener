package com.pascal.smart_notification_listener.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.get

private val avatarCache = mutableMapOf<String, Bitmap?>()

@SuppressLint("DiscouragedPrivateApi")
fun extractSafeAvatar(
    extras: Bundle?,
    n: Notification,
    ctx: Context
): Bitmap? {
    val senderName = extras?.getCharSequence("sender")?.toString()
        ?: extras?.getCharSequence("name")?.toString()
    if (!senderName.isNullOrBlank()) {
        avatarCache[senderName]?.let {
            return it
        }
    }

    val possibleKeys = listOf(
        "android.largeIcon.big",
        "android.largeIcon",
        "sender_icon",
        "sender_avatar",
        "person_icon",
        "android.person",
        "icon"
    )

    for (key in possibleKeys) {
        val parcel = extras?.get(key)
        if (parcel != null) {
            val bmp = when (parcel) {
                is Icon -> iconToBitmap(parcel, ctx)
                is Bitmap -> parcel
                else -> null
            }
            if (bmp != null) {
                if (!senderName.isNullOrBlank()) avatarCache[senderName] = bmp
                return bmp
            }
        }
    }

    val raw = runCatching {
        n.getLargeIcon()?.loadDrawable(ctx)?.toBitmap()
    }.getOrNull()

    val safe = raw?.let { ensureNotTransparent(it, ctx) }
    if (safe != null && !senderName.isNullOrBlank()) avatarCache[senderName] = safe as Bitmap?
    return safe
}

private fun ensureNotTransparent(bitmap: Bitmap, ctx: Context): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    var opaqueCount = 0
    var total = 0

    val stepX = maxOf(1, width / 32)
    val stepY = maxOf(1, height / 32)

    for (y in 0 until height step stepY) {
        for (x in 0 until width step stepX) {
            val alpha = (bitmap[x, y] shr 24) and 0xff
            if (alpha > 32) opaqueCount++
            total++
        }
    }

    if (opaqueCount < total / 10) {
        return createPlaceholderAvatar(ctx)
    }

    return bitmap
}

private fun createPlaceholderAvatar(ctx: Context): Bitmap {
    val size = (64 * ctx.resources.displayMetrics.density).toInt()
    val bmp = createBitmap(size, size)
    val canvas = Canvas(bmp)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.color = Color.LTGRAY
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
    return bmp
}

fun drawableToBitmap(drawable: Drawable): Bitmap {
    try {
        if (drawable is BitmapDrawable) {
            drawable.bitmap?.let { return it }
        }

        val width = when {
            drawable.intrinsicWidth > 0 -> drawable.intrinsicWidth
            else -> 64
        }
        val height = when {
            drawable.intrinsicHeight > 0 -> drawable.intrinsicHeight
            else -> width
        }

        val bmp = createBitmap(width, height)
        val canvas = Canvas(bmp)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bmp
    } catch (ex: Exception) {
        return createBitmap(48, 48)
    }
}

fun iconToBitmap(icon: Icon?, ctx: Context): Bitmap? {
    if (icon == null) return null
    return try {
        icon.loadDrawable(ctx)?.let { drawableToBitmap(it) }
    } catch (_: Exception) { null }
}

fun anyToBitmap(obj: Parcelable?, ctx: Context): Bitmap? {
    if (obj == null) return null
    return try {
        when (obj) {
            is Bitmap -> obj
            is Icon -> {
                runCatching {
                    val d =
                        obj.loadDrawable(ctx)
                    d?.let { drawableToBitmap(it) }
                }.getOrNull()
            }
            is Drawable -> drawableToBitmap(obj)
            is Bundle -> {
                val nestedKeys = listOf(Notification.EXTRA_LARGE_ICON_BIG, Notification.EXTRA_LARGE_ICON, "icon", "sender_icon")
                nestedKeys.forEach { k ->
                    val p = obj.getParcelable<Parcelable?>(k)
                    val b = anyToBitmap(p, ctx)
                    if (b != null) return b
                }
                null
            }
            else -> {
                val className = obj.javaClass.name
                null
            }
        }
    } catch (ex: Exception) {
        null
    }
}