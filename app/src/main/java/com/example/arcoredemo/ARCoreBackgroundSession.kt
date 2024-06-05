package com.example.arcoredemo

import com.google.ar.core.Frame
import com.google.ar.core.Session
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.*
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.arcoredemo.arcore.CameraPoseOutput
import com.example.arcoredemo.arcore.CameraTextureNameFactory
import com.example.arcoredemo.arcore.OpenGLAPI
import com.example.arcoredemo.equations.ArrowDirectionDetector
import java.util.Arrays


class ARCoreBackgroundSession(
    private val context: Context,
    val onUpdate: (CameraPoseOutput) -> Unit
) {
    private lateinit var session: Session
    private val openGLAPI = OpenGLAPI()

    private var isRunning = false
    private val thread = Thread {
        openGLAPI.setup()
        setupSession()
        setCameraTextureName()
        while (isRunning) {
            try {
                val frame = session.update()
                handleFrame(frame)
            } catch (e: CameraNotAvailableException) {
                e.printStackTrace()
                restartSession()
            }
        }
    }

    private fun setupSession() {
        session = Session(context)
        session.resume()
    }

    private fun setCameraTextureName() {
        val textureId = CameraTextureNameFactory.create()
        session.setCameraTextureName(textureId)
    }

    private fun restartSession() {
        try {
            session.resume()
            setCameraTextureName()
        } catch (e: CameraNotAvailableException) {
            e.printStackTrace()
            stop()
        }
    }

    fun start() {
        isRunning = true
        thread.start()
    }

    fun stop() {
        isRunning = false
        session.pause()
        openGLAPI.destroy()
    }

    private fun handleFrame(frame: Frame) {
        try {
            val cameraPose = frame.camera.pose
            val translation = cameraPose.translation
            val rotation = cameraPose.rotationQuaternion

            val translationString = Arrays.toString(translation)
            val rotationString = Arrays.toString(rotation)
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    "camera update $translationString $rotationString",
                    Toast.LENGTH_SHORT
                ).show()
            }
            val output = CameraPoseOutput.from(cameraPose)
            onUpdate(output)

            //pickScreenShotCamera(frame)
        } catch (e: NotYetAvailableException) {
            // Handle case where camera image is not yet available
        }
    }

    private fun pickScreenShotCamera(frame: Frame) {
        val cameraImage = frame.acquireCameraImage()
        cameraImage.close()
    }
}



