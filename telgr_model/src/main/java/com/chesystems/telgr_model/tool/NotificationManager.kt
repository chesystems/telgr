package com.chesystems.telgr_model.tool

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object NotificationMgr {
    private var notificationHelper: NotificationHelper? = null

    fun initialize(activity: ComponentActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request permission
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }

        if (notificationHelper == null) {
            notificationHelper = NotificationHelper(activity.applicationContext)
        }
    }

    fun showNotification(
        notificationId: Int,
        title: String,
        message: String
    ) {
        notificationHelper?.showNotification(
            notificationId = notificationId,
            title = title,
            message = message
        )
    }
}