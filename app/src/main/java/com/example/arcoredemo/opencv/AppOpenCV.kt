package com.example.arcoredemo.opencv

import android.util.Log
import org.opencv.android.OpenCVLoader
import org.opencv.calib3d.Calib3d
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.MatOfPoint2f
import org.opencv.imgproc.Imgproc

object AppOpenCV {

    fun setup() {
        val isInitialized = OpenCVLoader.initLocal()
        Log.d("AppOpenCV", "OpenCVLoader: isInitialized $isInitialized")
        debugEstimateAffinePartial2D()
    }

    private fun debugEstimateAffinePartial2D() {
        val srcPoints = MatOfPoint2f(
            Point(0.0, 0.0),
            Point(1.0, 0.0),
            Point(0.0, 1.0)
        )
        val dstPoints = MatOfPoint2f(
            Point(0.0, 0.0),
            Point(1.0, 1.0),
            Point(1.0, 0.0)
        )
        estimateAffinePartial2D(src = srcPoints, dst = dstPoints)
    }

    fun estimateAffinePartial2D(src: MatOfPoint2f, dst: MatOfPoint2f): Mat {
        val affineTransform = Calib3d.estimateAffinePartial2D(src, dst)
        Log.d("AppOpenCV", "Affine Partial Transform Matrix: ${affineTransform.dump()}")
        return affineTransform
    }
}
