package com.anjali.wedmoments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.anjali.wedmoments.data.AppRepository
import com.anjali.wedmoments.data.BookingEntity
import com.anjali.wedmoments.data.parseRupees

@Composable
fun MyBookingsScreen(
    repository: AppRepository,
    userId: Long,
    onBackClick: () -> Unit,
    onPaymentClick: (BookingEntity) -> Unit
) {
    val bookings by repository.observeBookings(userId).collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("← Back")
        }

        Text(text = "My Bookings", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Manage your wedding bookings",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (bookings.isEmpty()) {
            Text("No bookings yet. Book a creator from Home.")
        }

        bookings.forEach { booking ->
            val total = parseRupees(booking.packagePrice)
            val advance = if (total > 0) (total * 0.2).toInt() else 0
            val remaining = total - advance
            val isPaid = booking.status == "Confirmed"

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isPaid) "Confirmed Booking" else "Upcoming Booking",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        Text(text = "📸")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = booking.creatorName, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Wedding Date", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text(text = booking.weddingDate, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Venue", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text(text = booking.venue, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Package", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text(text = "${booking.packageName} - ${booking.packagePrice}", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Events", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text(text = "${booking.numberOfEvents} Events", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Booking Status", style = MaterialTheme.typography.labelMedium)
                        Text(
                            text = booking.status,
                            color = if (isPaid) Color(0xFF4CAF50) else Color(0xFFFF9800),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Package Price", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text(text = booking.packagePrice, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Advance Payment", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text(text = "₹$advance", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Remaining Amount", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text(text = "₹$remaining", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    if (!isPaid) {
                        Button(
                            onClick = { onPaymentClick(booking) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Make Payment")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
