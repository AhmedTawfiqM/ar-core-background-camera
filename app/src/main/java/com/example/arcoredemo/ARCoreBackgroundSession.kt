package com.example.arcoredemo

import com.google.ar.core.Frame
import com.google.ar.core.Session
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.UnavailableException
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLContext
import android.opengl.EGLDisplay
import android.opengl.EGLSurface
import com.google.ar.core.exceptions.*
import android.content.Context
import android.opengl.*
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import java.util.Arrays


class ARCoreBackgroundSession(
    private val context: Context,
    val onUpdate: (transition: FloatArray) -> Unit = {}
) {
    private lateinit var eglDisplay: EGLDisplay
    private lateinit var eglContext: EGLContext
    private lateinit var eglSurface: EGLSurface

    private lateinit var session: Session
    private var isRunning = false
    private var textureId: Int = -1
    private val thread = Thread {
        try {
            initGL()
            session = Session(context)
            session.resume()
            setCameraTextureName()
            while (isRunning) {
                try {
                    val frame: Frame = session.update()
                    handleFrame(frame)
                } catch (e: CameraNotAvailableException) {
                    e.printStackTrace()
                    restartSession()
                }
            }
        } catch (e: UnavailableException) {
            e.printStackTrace()
        } catch (e: MissingGlContextException) {
            e.printStackTrace()
        } catch (e: TextureNotSetException) {
            e.printStackTrace()
        }
    }


    private fun initGL() {
        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        val version = IntArray(2)
        EGL14.eglInitialize(eglDisplay, version, 0, version, 1)

        val attribList = intArrayOf(
            EGL14.EGL_RED_SIZE, 8,
            EGL14.EGL_GREEN_SIZE, 8,
            EGL14.EGL_BLUE_SIZE, 8,
            EGL14.EGL_ALPHA_SIZE, 8,
            EGL14.EGL_DEPTH_SIZE, 16,
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL14.EGL_NONE
        )
        val configs = arrayOfNulls<EGLConfig>(1)
        val numConfigs = IntArray(1)
        EGL14.eglChooseConfig(eglDisplay, attribList, 0, configs, 0, 1, numConfigs, 0)

        val contextAttribList = intArrayOf(
            EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL14.EGL_NONE
        )
        eglContext = EGL14.eglCreateContext(
            eglDisplay,
            configs[0],
            EGL14.EGL_NO_CONTEXT,
            contextAttribList,
            0
        )

        val surfaceAttribList = intArrayOf(
            EGL14.EGL_WIDTH, 1,
            EGL14.EGL_HEIGHT, 1,
            EGL14.EGL_NONE
        )
        eglSurface = EGL14.eglCreatePbufferSurface(eglDisplay, configs[0], surfaceAttribList, 0)

        EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)
    }

    private fun setCameraTextureName() {
        // Generate a texture ID
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        textureId = textures[0]

        // Bind the texture
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_LINEAR
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR
        )

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
        EGL14.eglDestroySurface(eglDisplay, eglSurface)
        EGL14.eglDestroyContext(eglDisplay, eglContext)
        EGL14.eglTerminate(eglDisplay)
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
            updateObjectPosition(translation, rotation)

            pickScreenShotCamera(frame)
        } catch (e: NotYetAvailableException) {
            // Handle case where camera image is not yet available
        }
    }

    private fun pickScreenShotCamera(frame: Frame) {
        val cameraImage = frame.acquireCameraImage()
        cameraImage.close()
    }


    private fun updateObjectPosition(translation: FloatArray, rotation: FloatArray) {
        onUpdate(translation)
        /** translation[0], translation[1], translation[2] give you the x, y, z coordinates
        // rotation[0], rotation[1], rotation[2], rotation[3] give you the quaternion rotation
         */
    }
}



