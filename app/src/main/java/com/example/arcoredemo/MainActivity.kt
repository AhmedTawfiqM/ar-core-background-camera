package com.example.arcoredemo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.arcoredemo.arcore.ARCorePermission
import com.example.arcoredemo.databinding.ActivityMainBinding
import com.example.arcoredemo.opencv.AppOpenCV
import org.opencv.android.OpenCVLoader
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var arCoreBackgroundSession = ARCoreBackgroundSession(this) { transition, direction ->
        movePinMarker(transition, direction)
        estimateAffinePartial2D(transition)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ARCorePermission(this).setup()
        AppOpenCV.setup()
    }

    private fun movePinMarker(translation: FloatArray, direction: String) {
        val x = translation[0] * 100
        val y = translation[2] * 100

        binding.pinMarker.translationX = x
        binding.pinMarker.translationY = y

        Handler(Looper.getMainLooper()).post {
            binding.tvDirection.text = "Direction: $direction"
        }
    }

    override fun onPause() {
        super.onPause()
        arCoreBackgroundSession.stop()
    }

    override fun onResume() {
        super.onResume()
        arCoreBackgroundSession.start()
    }

    private fun estimateAffinePartial2D(transition: FloatArray) {
        try {
            val transitionX = transition[0].toDouble()
            val transitionY = transition[1].toDouble()
            val srcMatrix = MatOfPoint2f(Point(transitionX, transitionY))

            val arrowX = binding.pinMarker.x.toDouble()
            val arrowY = binding.pinMarker.y.toDouble()
            val dstMatrix = MatOfPoint2f(Point(arrowX, arrowY))

            val matrix = AppOpenCV.estimateAffinePartial2D(src = srcMatrix, dst = dstMatrix)
            Log.d("AppOpenCV", "estimateAffinePartial2D: ${matrix.dump()}")
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.d("AppOpenCV", "Exception: ${ex.message}")
        }
    }
}