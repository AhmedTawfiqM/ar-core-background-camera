package com.example.arcoredemo.opencv

import android.util.Log
import org.opencv.android.OpenCVLoader

object AppOpenCV {

    fun setup() {
        val isInitialized = OpenCVLoader.initLocal()
        Log.d("AppOpenCV", "OpenCVLoader: isInitialized $isInitialized")
    }
}