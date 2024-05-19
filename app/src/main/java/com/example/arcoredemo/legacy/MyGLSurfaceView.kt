package com.example.arcoredemo.legacy

import android.content.Context
import android.opengl.GLSurfaceView
import com.google.ar.core.Session


class MyGLSurfaceView(context: Context, session: Session) : GLSurfaceView(context) {

    init {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        val renderer = MyGLRenderer()
        renderer.setSession(session)
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)

        // Render the view only when there is a change in the drawing data
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }
}
