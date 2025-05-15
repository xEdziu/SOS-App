package pwr.goral.sosapp

import android.hardware.camera2.CameraManager
import kotlinx.coroutines.*

class SosFlasher (
    private val cameraManager: CameraManager,
    private val cameraId: String
) {
    private var job: Job? = null

    fun start() {
        job = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                // sekwencja SOS
                flashDot()
                flashDot()
                flashDot()
                flashDash()
                flashDash()
                flashDash()
            }
        }
    }

    fun stop() {
        job?.cancel()
        try { cameraManager.setTorchMode(cameraId, false) } catch (_: Exception) {}
    }

    private suspend fun flashDot() {
        cameraManager.setTorchMode(cameraId, true)
        delay(500)
        cameraManager.setTorchMode(cameraId, false)
        delay(500)
    }

    private suspend fun flashDash() {
        cameraManager.setTorchMode(cameraId, true)
        delay(1500)
        cameraManager.setTorchMode(cameraId, false)
        delay(500)
    }
}