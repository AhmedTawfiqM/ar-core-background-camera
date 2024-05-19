package com.example.arcoredemo

import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.google.ar.core.Session
import com.google.ar.core.exceptions.MissingGlContextException
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer : GLSurfaceView.Renderer {
    private lateinit var session: Session

    fun setSession(session: Session) {
        this.session = session
    }

    override fun onSurfaceCreated(gl: GL10, config: javax.microedition.khronos.egl.EGLConfig?) {
        // Prepare shaders and OpenGL program objects here
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f)
        // Initialize ARCore session objects that depend on OpenGL context here
    }

    override fun onDrawFrame(gl: GL10) {
        // Make sure to update and draw ARCore objects here
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        try {
            val textures = IntArray(1)
            GLES20.glGenTextures(1, textures, 0)
            val cameraTextureId = textures[0]
            val textureTarget = GLES11Ext.GL_TEXTURE_EXTERNAL_OES
            GLES20.glBindTexture(textureTarget, cameraTextureId)
            GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
            GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
            GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
            GLES20.glTexParameteri(textureTarget, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

            session.setCameraTextureName(cameraTextureId)
            val frame = session.update()
            // Process the frame to render AR content
        } catch (e: MissingGlContextException) {
            e.printStackTrace()
        }
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }
}
