package pwr.goral.sosapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun AuthorScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Imię i nazwisko: Adrian Goral",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Uczelnia: Politechnika Wrocławska",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "VI semestr, Maj 2025",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Kierunek: Informatczne Systemy Automatyki",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
