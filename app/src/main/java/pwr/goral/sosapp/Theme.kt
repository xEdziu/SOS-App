package pwr.goral.sosapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta jasnego motywu – delikatne odcienie czerwieni jako akcent
private val LightColors = lightColorScheme(
    primary = Color(0xFFE57373),   // pastelowy czerwony (akcent główny jasny)
    onPrimary = Color.White,
    secondary = Color(0xFFEF5350), // dodatkowy odcień czerwieni
    onSecondary = Color.White
    // Można zdefiniować inne kolory (np. tło, powierzchni) lub skorzystać z domyślnych
)

// Paleta ciemnego motywu – jaśniejszy czerwony dla lepszej widoczności na ciemnym tle
private val DarkColors = darkColorScheme(
    primary = Color(0xFFEF9A9A),   // jaśniejszy czerwony (akcent w trybie ciemnym)
    onPrimary = Color.Black,
    secondary = Color(0xFFE57373),
    onSecondary = Color.Black
)

@Composable
fun SOSAppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Wybór palety kolorów na podstawie ustawień systemowych (ciemny/jasny):contentReference[oaicite:6]{index=6}
    val colors = if (!useDarkTheme) LightColors else DarkColors

    MaterialTheme(
        colorScheme = colors,
        // typography i shapes mogą być podane własne lub użyte domyślne Material3
        content = content
    )
}
