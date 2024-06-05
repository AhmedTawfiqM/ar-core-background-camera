package com.example.arcoredemo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.arcoredemo.arcore.ARCorePermission
import com.example.arcoredemo.arcore.CameraPoseOutput
import com.example.arcoredemo.databinding.ActivityMainBinding
import com.example.arcoredemo.opencv.AppOpenCV
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var arCoreBackgroundSession = ARCoreBackgroundSession(this) { output ->
        movePinMarker(output)
        estimateAffinePartial2D(output)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ARCorePermission(this).setup()
        AppOpenCV.setup()
    }

    private fun movePinMarker(output: CameraPoseOutput) {
        val x = output.translationX * 100
        val y = output.translationY * 100

        binding.pinMarker.translationX = x
        binding.pinMarker.translationY = y
    }

    override fun onPause() {
        super.onPause()
        arCoreBackgroundSession.stop()
    }

    override fun onResume() {
        super.onResume()
        arCoreBackgroundSession.start()
    }

    private fun estimateAffinePartial2D(output: CameraPoseOutput) {
        try {
            val transitionX = output.translationX.toDouble()
            val transitionY = output.translationY.toDouble()
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