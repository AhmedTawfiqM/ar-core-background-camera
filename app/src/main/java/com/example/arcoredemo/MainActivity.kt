package com.example.arcoredemo

import android.os.Bundle
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
        arCoreBackgroundSession = ARCoreBackgroundSession(this) {
            movePinMarker(it)
        }
    }

    private fun movePinMarker(translation: FloatArray) {
        val x = translation[0] * 100
        val y = translation[2] * 100

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
}