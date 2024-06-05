package com.example.arcoredemo.arcore

import com.google.ar.core.Pose

data class CameraPoseOutput(
    val translation: List<Float>,
    val quaternion: List<Float>,
) {

    val translationX: Float
        get() = translation[0]

    val translationY: Float
        get() = translation[2]

    companion object {

        fun from(cameraPose: Pose): CameraPoseOutput {
            val translation = cameraPose.translation
            val rotationQuaternion = cameraPose.rotationQuaternion

            return CameraPoseOutput(
                translation = translation.asList(),
                quaternion = rotationQuaternion.asList(),
            )
        }
    }
}