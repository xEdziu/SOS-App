package pwr.goral.sosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SOSAppTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    // Konfiguracja kontrolera nawigacji Compose
    val navController = rememberNavController()
    // Stan szuflady nawigacji (otwarta/zamknięta)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    // Obserwacja aktualnej destynacji, aby podświetlać wybraną pozycję i ustawiać tytuł
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                currentRoute = currentRoute,
                onItemSelected = { route ->

                    // Nawigacja do wybranej sekcji i zamknięcie szuflady
                    navController.navigate(route) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                    }
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                // Górny pasek aplikacji z ikoną menu (hamburger) otwierającą szufladę
                TopAppBar(
                    title = { Text(when (currentRoute) {
                        "map" -> "Mapa z Twoją pozycją"
                        "author" -> "Autor programu"
                        else -> "Sygnał SOS"
                    }) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Otwórz menu")
                        }
                    }
                )
            },
            content = { innerPadding ->
                // Kontener nawigacji między ekranami za pomocą NavHost
                NavHost(
                    navController = navController,
                    startDestination = "sos",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("sos") { SosScreen() }
                    composable("map") { MapScreen() }
                    composable("author") { AuthorScreen() }
                }
            }
        )
    }
}
