package com.pascal.smartnotificationlistener

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pascal.smart_notification_listener.utils.NotificationPermissionUtils
import com.pascal.smart_notification_listener.utils.SmartNotificationListener
import com.pascal.smartnotificationlistener.ui.screen.NotificationScreen
import com.pascal.smartnotificationlistener.ui.screen.PermissionScreen
import com.pascal.smartnotificationlistener.ui.theme.SmartNotificationListenerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartNotificationListenerTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var permissionGranted by remember {
        mutableStateOf(false)
    }

    fun checkPermission() {
        permissionGranted =
            NotificationPermissionUtils.isGranted(context)
    }

    LaunchedEffect(Unit) {
        checkPermission()
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                checkPermission()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (permissionGranted) {
        NotificationScreen()
    } else {
        PermissionScreen(
            onEnableClick = {
                SmartNotificationListener.requestPermission(context)
            }
        )
    }
}