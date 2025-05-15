package pwr.goral.sosapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.content.res.Configuration
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
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
    val context = LocalContext.current
    val smsManager = LocalContext.current.getSystemService(SmsManager::class.java)

    var lastLocation by remember { mutableStateOf<LatLng?>(null) }
    var pendingSend by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // użytkownik zaakceptował – wykonaj akcję
            sendSmsWithLocation(context, smsManager, lastLocation)
        } else {
            Toast.makeText(context, "Odmówiono dostępu do SMS", Toast.LENGTH_SHORT).show()
        }
        pendingSend = false
    }

    // Pobierz lokalizację (nie wymaga uprawnień SMS)
    LaunchedEffect(Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val cancellationTokenSource = CancellationTokenSource()
        try {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    lastLocation = LatLng(location.latitude, location.longitude)
                } else {
                    fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc ->
                        if (lastLoc != null) {
                            lastLocation = LatLng(lastLoc.latitude, lastLoc.longitude)
                        }
                    }
                }
            }
        } catch (_: SecurityException) {}
    }

    Button(
        onClick = {
            val permission = android.Manifest.permission.SEND_SMS
            val granted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
            if (granted) {
                sendSmsWithLocation(context, smsManager, lastLocation)
            } else {
                pendingSend = true
                launcher.launch(permission)
            }
        },
        modifier = modifier
    ) {
        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Wyślij SMS")
        Spacer(Modifier.width(8.dp))
        Text("Wyślij SMS")
    }
}

fun sendSmsWithLocation(context: Context, smsManager: SmsManager, location: LatLng?) {
    val phoneNumber = "" // zmień na docelowy
    val locationText = location?.let { "https://maps.google.com/?q=${it.latitude},${it.longitude}" }
        ?: "brak lokalizacji"
    val messageZero = "Wiadomość testowa. Zignoruj."
    val messageFirst = "SOS! Potrzebuję pomocy!\nWysłano z aplikacji SOS App"
    val messageSecond = "Moja lokalizacja: $locationText"
    try {
        // Wysyłanie SMS
        Log.d("SOS", "Wysyłanie SMS do $phoneNumber: $messageZero")
        smsManager.sendTextMessage(phoneNumber, null, messageZero, null, null)
        Log.d("SOS", "Wysyłanie SMS do $phoneNumber: $messageFirst")
        smsManager.sendTextMessage(phoneNumber, null, messageFirst, null, null)
        Log.d("SOS", "Wysyłanie SMS do $phoneNumber: $messageSecond")
        smsManager.sendTextMessage(phoneNumber, null, messageSecond, null, null)
        Toast.makeText(context, "Wysłano SMS", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Błąd przy wysyłaniu SMS", Toast.LENGTH_LONG).show()
        Log.e("SMS", "Błąd: ${e.message}")
    }
}

