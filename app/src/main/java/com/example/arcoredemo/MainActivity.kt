package com.example.arcoredemo

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.arcoredemo.databinding.ActivityMainBinding
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Config
import com.google.ar.core.Session


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var session: Session
    private lateinit var glView: MyGLSurfaceView
    private lateinit var arCoreBackgroundSession: ARCoreBackgroundSession
    private val scalingFactor = 0.1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        maybeEnableArButton()
//        setup()
//        glView = MyGLSurfaceView(this,session)

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


    private fun setup() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 0)
        }

        session = Session(this)
    }

    override fun onPause() {
        super.onPause()
        //session.pause()
        arCoreBackgroundSession.stop()
    }

    override fun onResume() {
        super.onResume()
//        configureSession()
//        session.resume()
//        startSession()
        arCoreBackgroundSession.start()
    }

    private fun configureSession(){
        val config = session.config
        config.setLightEstimationMode(Config.LightEstimationMode.ENVIRONMENTAL_HDR)
        if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
            config.setDepthMode(Config.DepthMode.AUTOMATIC)
        } else {
            config.setDepthMode(Config.DepthMode.DISABLED)
        }
        session.configure(config)
    }

    fun startSession(){
        try {
            val frame = session.update()
            val pose = frame.camera.pose
        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }

    private fun maybeEnableArButton() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 0)
        }

        when (ArCoreApk.getInstance().requestInstall(this, true)) {
            ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {}
            ArCoreApk.InstallStatus.INSTALLED -> {}
        }
        ArCoreApk.getInstance().checkAvailabilityAsync(this) { availability ->
            Log.d("availability", availability.isSupported.toString())
        }
    }

}