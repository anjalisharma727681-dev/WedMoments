package com.anjali.wedmoments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anjali.wedmoments.data.AppRepository

@Composable
fun BookingSuccessScreen(
    repository: AppRepository,
    bookingId: Long,
    creatorName: String,
    onHomeClick: () -> Unit
) {
    val booking by repository.observeBooking(bookingId).collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F5FC))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "✓", fontSize = 60.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6750A4))
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Booking Confirmed!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your wedding creator has been booked successfully.",
            fontSize = 15.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(25.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "Booking Details", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "Creator", fontWeight = FontWeight.Bold)
                Text(text = booking?.creatorName ?: creatorName, color = Color(0xFF6750A4))
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Status", fontWeight = FontWeight.Bold)
                Text(text = booking?.status ?: "Pending", color = Color(0xFFFF9800))
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Booking ID", fontWeight = FontWeight.Bold)
                Text(text = if (bookingId > 0) "WM$bookingId" else "WM-")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onHomeClick,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4))
        ) {
            Text(text = "Back to Home", fontSize = 17.sp)
        }
    }
}
