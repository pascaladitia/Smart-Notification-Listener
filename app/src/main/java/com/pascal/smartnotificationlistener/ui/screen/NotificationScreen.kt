package com.pascal.smartnotificationlistener.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pascal.smart_notification_listener.utils.SmartNotificationListener
import com.pascal.smartnotificationlistener.ui.component.EmptyState
import com.pascal.smartnotificationlistener.ui.component.NotificationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier
) {

    val notifications by SmartNotificationListener
        .notifications
        .collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Smart Notification Listener") }
            )
        }
    ) { padding ->

        if (notifications.isEmpty()) {
            EmptyState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = notifications,
                    key = { "${it.packageName}-${it.id}-${it.postedAt}" }
                ) {
                    NotificationItem(it)
                }
            }
        }
    }
}
