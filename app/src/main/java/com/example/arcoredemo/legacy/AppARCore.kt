package com.example.arcoredemo.legacy

import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.core.Pose
import com.google.ar.core.Session
import com.google.ar.core.TrackingState

class AppARCore(private val activity: FragmentActivity, private val pinImageView: ImageView) {
    private var lastPose: Pose? = null

    init {
        setup()
    }

    private fun setup() {
        val session = Session(activity)
        val frame = session.update()
        val pose = frame.camera.pose
        lastPose = pose
    }

    private fun updatePinPosition(dx: Float, dy: Float) {
        // Update your pin marker view here based on the delta
        // Assuming you have a method to update UI on the main thread
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post {
            // Assuming you have access to the ImageView of the pin
            pinImageView.x += dx  // scaleFactor to adjust the sensitivity
            pinImageView.y += dy
        }
    }
}

