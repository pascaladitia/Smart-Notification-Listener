package com.pascal.smartnotificationlistener.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PermissionScreen(
    onEnableClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text("Notification Access Required")
        androidx.compose.foundation.layout.Spacer(
            Modifier.padding(8.dp)
        )
        androidx.compose.material3.Button(onClick = onEnableClick) {
            Text("Enable Notification Access")
        }
    }
}
