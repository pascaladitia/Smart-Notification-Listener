package com.pascal.smart_notification_listener.core

import com.pascal.smart_notification_listener.model.NotifMeta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal object NotifRepository {

    private val _notifications = MutableStateFlow<List<NotifMeta>>(emptyList())
    val notifications: StateFlow<List<NotifMeta>> = _notifications.asStateFlow()

    fun setAll(list: List<NotifMeta>) {
        _notifications.value = list
    }

    fun addOrUpdate(meta: NotifMeta) {
        val current = _notifications.value.toMutableList()
        val index = current.indexOfFirst {
            it.id == meta.id && it.packageName == meta.packageName
        }

        if (index >= 0) {
            current[index] = meta
        } else {
            current += meta
        }

        _notifications.value = current
    }

    fun remove(id: Int, pkg: String) {
        _notifications.value =
            _notifications.value.filterNot { it.id == id && it.packageName == pkg }
    }
}
