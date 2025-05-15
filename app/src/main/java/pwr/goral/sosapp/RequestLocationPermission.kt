package pwr.goral.sosapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch

@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var permissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var shouldShowRationale by remember { mutableStateOf(false) }
    var shouldOpenSettings by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
        shouldShowRationale = !isGranted
        shouldOpenSettings = !isGranted && !shouldShowRationale
        if (isGranted) onPermissionGranted() else onPermissionDenied()
    }

    LaunchedEffect(Unit) {
        if (!permissionGranted) {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    if (shouldShowRationale) {
        LaunchedEffect(Unit) {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "Aplikacja potrzebuje dostępu do lokalizacji, by móc oferować pełną funkcjonalność.\n" +
                            "Czy chcesz otworzyć okno ustawień?",
                    actionLabel = "Zezwól"
                )
                if (result == SnackbarResult.ActionPerformed) {
                    shouldShowRationale = false
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }

    if (shouldOpenSettings) {
        // Otwórz ustawienia aplikacji
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )
        context.startActivity(intent)
    }

    // Dodaj SnackbarHost do swojego ekranu
    SnackbarHost(hostState = snackbarHostState)
}