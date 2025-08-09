package com.rekognizevote.core.permissions

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager

@Composable
fun PermissionHandler(
    permission: String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    
    val hasPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    if (hasPermission) {
        LaunchedEffect(Unit) {
            onPermissionGranted()
        }
    } else {
        LaunchedEffect(Unit) {
            onPermissionDenied()
        }
    }
}