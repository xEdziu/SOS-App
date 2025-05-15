package pwr.goral.sosapp

import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DrawerContent(
    currentRoute: String?,
    onItemSelected: (String) -> Unit
) {
    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))
        // Pozycja 1: Sygnał SOS
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Warning, contentDescription = "SOS") },
            label = { Text("Sygnał SOS") },
            selected = currentRoute == "sos",
            onClick = { onItemSelected("sos") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        // Pozycja 2: Mapa
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.LocationOn, contentDescription = "Mapa") },
            label = { Text("Mapa") },
            selected = currentRoute == "map",
            onClick = { onItemSelected("map") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        // Oddzielenie sekcji „Autor”
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
        // Pozycja 3: Autor
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "O autorze") },
            label = { Text("Autor") },
            selected = currentRoute == "author",
            onClick = { onItemSelected("author") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}
