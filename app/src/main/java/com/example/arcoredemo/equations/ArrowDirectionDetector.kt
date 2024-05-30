package com.example.arcoredemo.equations

import kotlin.math.abs

object ArrowDirectionDetector {
    fun detect(translation: FloatArray, rotation: FloatArray): String {
        if (translation.size < 3 || rotation.size < 4) {
            throw IllegalArgumentException("Invalid translation or rotation array size")
        }

        val x = translation[0]
        val y = translation[1]
        val z = translation[2]

        //TODO: may be deleted ,since it's not used !
        // Placeholder for rotation usage if needed
        val qw = rotation[0]
        val qx = rotation[1]
        val qy = rotation[2]
        val qz = rotation[3]

        val threshold = 0.1f //0f

        return when {
            x > threshold -> "Right"
            x < -threshold -> "Left"
            y > threshold -> "Up"
            y < -threshold -> "Down"
            z > threshold -> "Forward"
            z < -threshold -> "Backward"
            else -> "Stable"
        }
    }

    fun detectMethod2(translation: FloatArray, rotation: FloatArray): String {
        if (translation.size < 3 || rotation.size < 4) {
            throw IllegalArgumentException("Invalid translation or rotation array size")
        }

        val threshold = 0.1f

        val x = translation[0]
        val y = translation[1]
        val z = translation[2]

        // Find the dominant direction of movement
        val absX = abs(x)
        val absY = abs(y)
        val absZ = abs(z)

        return when {
            absX > absY && absX > absZ && absX > threshold -> {
                if (x > 0) "Right" else "Left"
            }
            absY > absX && absY > absZ && absY > threshold -> {
                if (y > 0) "Up" else "Down"
            }
            absZ > absX && absZ > absY && absZ > threshold -> {
                if (z > 0) "Forward" else "Backward"
            }
            else -> "Stable"
        }
    }
}

