package pwr.goral.sosapp

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen() {
    val context = LocalContext.current

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var loading by remember { mutableStateOf(!hasPermission) }

    RequestLocationPermission(
        onPermissionGranted = {
            hasPermission = true
            loading = true // Rozpocznij ładowanie lokalizacji
        },
        onPermissionDenied = {
            hasPermission = false
            loading = false
        }
    )

    // Pobranie lokalizacji (asynchronicznie)
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            val cancellationTokenSource = CancellationTokenSource()

            try {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                ).addOnSuccessListener { location ->
                    if (location != null) {
                        userLocation = LatLng(location.latitude, location.longitude)
                    } else {
                        // fallback: użyj lastLocation
                        fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc ->
                            if (lastLoc != null) {
                                userLocation = LatLng(lastLoc.latitude, lastLoc.longitude)
                            } else {
                                Toast.makeText(context, "Nie udało się pobrać lokalizacji - toast 3", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    loading = false
                }
            } catch (e: SecurityException) {
                Toast.makeText(context, "Brak uprawnień do lokalizacji - toast 1", Toast.LENGTH_SHORT).show()
                loading = false
            }
        }
    }

    if (!hasPermission && !loading) {
        Toast.makeText(context, "Brak uprawnień do lokalizacji - toast 2", Toast.LENGTH_SHORT).show()
        return
    }

    if (loading || userLocation == null) {
        // Spinner do czasu załadowania
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Gdy lokalizacja znana – pokaż mapę
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation!!, 15f)
    }

    val markerState = remember { MarkerState(position = userLocation!!) }


    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(myLocationButtonEnabled = true)
    ) {
        Marker(
            state = markerState,
            title = "Twoja lokalizacja"
        )
    }
}
