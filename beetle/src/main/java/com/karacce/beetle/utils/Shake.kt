package com.karacce.beetle.utils

import android.content.Context
import android.hardware.SensorManager
import java.util.concurrent.TimeUnit

/**
 * @user: omerkaraca
 * @date: 2019-07-06
 */

class Shake(private val listener: ShakeDetector.Listener): ShakeDetector.Listener {

    private val detector: ShakeDetector = ShakeDetector(this)
    private val coolDown: Long = TimeUnit.SECONDS.toMillis(5)
    private var lastShakeTime: Long = 0

    init {
        detector.setSensitivity(ShakeDetector.SENSITIVITY_MEDIUM)
    }

    fun start(context: Context) {
        detector.start(context.getSystemService(Context.SENSOR_SERVICE) as SensorManager)
    }

    fun stop() {
        detector.stop()
    }

    fun testShake() {
        onShake()
    }

    override fun onShake() {
        if ((System.currentTimeMillis() >= lastShakeTime + coolDown)) {
            lastShakeTime = System.currentTimeMillis()
            listener.onShake()
        }
    }
}