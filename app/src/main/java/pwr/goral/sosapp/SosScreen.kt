package pwr.goral.sosapp

import android.content.Context
import android.content.res.Configuration
import android.hardware.camera2.CameraManager
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun SosScreen() {

    val cameraManager = LocalContext.current.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val cameraId = cameraManager.cameraIdList[0]

    val orientation = LocalConfiguration.current.orientation
    Box(modifier = Modifier.fillMaxSize()) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // W poziomie – dwa przyciski obok siebie
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SosButton(modifier = Modifier.weight(1f), cameraManager, cameraId)
                SmsButton(modifier = Modifier.weight(1f))
            }
        } else {
            // W pionie – dwa przyciski jeden pod drugim
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SosButton(modifier = Modifier.fillMaxWidth(), cameraManager, cameraId)
                SmsButton(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun SosButton(modifier: Modifier = Modifier, cameraManager: CameraManager, cameraId: String) {

    val isSosOn = remember { mutableStateOf(false) }
    val buttonText = if (isSosOn.value) "Wyłącz SOS" else "Uruchom SOS"
    val sosFlasher = remember { SosFlasher(cameraManager, cameraId) }

    androidx.compose.runtime.DisposableEffect(Unit) {
        onDispose {
            sosFlasher.stop()
        }
    }

    Button(
        onClick = {
            if (isSosOn.value) {
                sosFlasher.stop()
                isSosOn.value = false
            } else {
                sosFlasher.start()
                isSosOn.value = true
            }
        },
        modifier = modifier
    ) {
        Icon(Icons.Default.Star, contentDescription = "Latarka SOS")
        Spacer(Modifier.width(8.dp))
        Text(buttonText)
    }
}

@Composable
fun SmsButton(modifier: Modifier = Modifier) {
    Button(
        onClick = {
            // TODO: zaimplementuj wysyłanie SMS z prośbą o pomoc (np. z lokalizacją)
        },
        modifier = modifier
    ) {
        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Wyślij SMS")
        Spacer(Modifier.width(8.dp))
        Text("Wyślij SMS")
    }
}
