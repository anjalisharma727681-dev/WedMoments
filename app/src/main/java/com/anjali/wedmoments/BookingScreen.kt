package com.anjali.wedmoments

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.anjali.wedmoments.data.CreatorEntity
import com.anjali.wedmoments.data.PackageEntity
import kotlinx.coroutines.launch

@Composable
fun BookingScreen(
    creator: CreatorEntity,
    selectedPackage: PackageEntity?,
    onBackClick: () -> Unit,
    onBookingConfirmed: suspend (weddingDate: String, venue: String, events: String, requirements: String) -> Unit
) {
    var weddingDate by remember { mutableStateOf("") }
    var venue by remember { mutableStateOf("") }
    var numberOfEvents by remember { mutableStateOf("") }
    var specialRequirements by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("← Back")
        }

        Text(text = "Book Creator", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = creator.name, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Selected Package", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                if (selectedPackage != null) {
                    Text(text = selectedPackage.name, style = MaterialTheme.typography.titleLarge)
                    Text(text = selectedPackage.price, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("📸 ${selectedPackage.photos}")
                    Text("🎬 ${selectedPackage.reels}")
                    Text("🎥 ${selectedPackage.videos}")
                    Text("⏱ ${selectedPackage.hours}")
                } else {
                    Text("No package selected")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = weddingDate,
            onValueChange = { weddingDate = it },
            label = { Text("Wedding Date") },
            placeholder = { Text("DD/MM/YYYY") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = venue,
            onValueChange = { venue = it },
            label = { Text("Venue") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = numberOfEvents,
            onValueChange = { numberOfEvents = it },
            label = { Text("Number of Events") },
            placeholder = { Text("Example: 3") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = specialRequirements,
            onValueChange = { specialRequirements = it },
            label = { Text("Special Requirements") },
            placeholder = { Text("Enter your requirements") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (selectedPackage == null) {
                    Toast.makeText(context, "Select a package first", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (weddingDate.isBlank() || venue.isBlank() || numberOfEvents.isBlank()) {
                    Toast.makeText(context, "Fill date, venue and events", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                scope.launch {
                    onBookingConfirmed(weddingDate, venue, numberOfEvents, specialRequirements)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Book Now")
        }
    }
}
