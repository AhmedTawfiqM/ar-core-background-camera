package com.example.arcoredemo.arcore

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.ar.core.ArCoreApk

class ARCorePermission(private val activity: Activity) {

    fun setup() {
        requestCamera()
        checkARCoreEnabled()
    }

    private fun requestCamera() {
        if (ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.CAMERA),
                0
            )
        }
    }

    private fun checkARCoreEnabled() {
        when (ArCoreApk.getInstance().requestInstall(activity, true)) {
            ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {}
            ArCoreApk.InstallStatus.INSTALLED -> {}
        }
        ArCoreApk.getInstance().checkAvailabilityAsync(activity) { availability ->
            Log.d("availability", availability.isSupported.toString())
        }
    }
}