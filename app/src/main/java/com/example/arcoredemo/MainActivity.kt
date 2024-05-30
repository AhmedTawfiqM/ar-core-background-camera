package com.example.arcoredemo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.arcoredemo.arcore.ARCorePermission
import com.example.arcoredemo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var arCoreBackgroundSession: ARCoreBackgroundSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ARCorePermission(this).setup()
        arCoreBackgroundSession = ARCoreBackgroundSession(this) { transition, direction ->
            movePinMarker(transition, direction)
        }
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
}