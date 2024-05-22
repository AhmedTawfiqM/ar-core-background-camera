package com.example.arcoredemo.arcore

import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLContext
import android.opengl.EGLDisplay
import android.opengl.EGLSurface

class OpenGLAPI {
    private lateinit var eglDisplay: EGLDisplay
    private lateinit var eglContext: EGLContext
    private lateinit var eglSurface: EGLSurface

    private val majorOffset = 0
    private val minorOffset = 1
    private val configSize = 1
    private val numConfigOffset = 0

    private val defaultSurfaceAttribute = 1
    private val defaultContextAttribute = 2
    private val attributeSize8 = 8
    private val attributeSize16 = 16

    fun setup() {
        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        val version = IntArray(2)
        EGL14.eglInitialize(eglDisplay, version, majorOffset, version, minorOffset)

        val attribList = generateAttributeList()
        val configs = arrayOfNulls<EGLConfig>(1)
        val numConfigs = IntArray(1)
        EGL14.eglChooseConfig(
            eglDisplay,
            attribList,
            majorOffset,
            configs,
            majorOffset,
            configSize,
            numConfigs,
            numConfigOffset
        )

        val contextAttribList = intArrayOf(
            EGL14.EGL_CONTEXT_CLIENT_VERSION,
            defaultContextAttribute,
            EGL14.EGL_NONE
        )
        val defaultConfig = configs.first()
        eglContext = EGL14.eglCreateContext(
            eglDisplay,
            defaultConfig,
            EGL14.EGL_NO_CONTEXT,
            contextAttribList,
            majorOffset
        )

        val surfaceAttribList = intArrayOf(
            EGL14.EGL_WIDTH,
            defaultSurfaceAttribute,
            EGL14.EGL_HEIGHT,
            defaultSurfaceAttribute,
            EGL14.EGL_NONE
        )
        eglSurface = EGL14.eglCreatePbufferSurface(
            eglDisplay,
            defaultConfig,
            surfaceAttribList,
            majorOffset,
        )

        EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)
    }

    private fun generateAttributeList(): IntArray {
        return intArrayOf(
            EGL14.EGL_RED_SIZE,
            attributeSize8,
            EGL14.EGL_GREEN_SIZE,
            attributeSize8,
            EGL14.EGL_BLUE_SIZE,
            attributeSize8,
            EGL14.EGL_ALPHA_SIZE,
            attributeSize8,
            EGL14.EGL_DEPTH_SIZE,
            attributeSize16,
            EGL14.EGL_RENDERABLE_TYPE,
            EGL14.EGL_OPENGL_ES2_BIT,
            EGL14.EGL_NONE
        )
    }

    fun destroy() {
        EGL14.eglDestroySurface(eglDisplay, eglSurface)
        EGL14.eglDestroyContext(eglDisplay, eglContext)
        EGL14.eglTerminate(eglDisplay)
    }
}
