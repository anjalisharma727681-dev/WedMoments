package com.anjali.wedmoments

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anjali.wedmoments.data.AppRepository
import com.anjali.wedmoments.data.parseRupees
import kotlinx.coroutines.launch

@Composable
fun PaymentScreen(
    repository: AppRepository,
    bookingId: Long,
    creatorName: String,
    eventType: String,
    onBackClick: () -> Unit,
    onPaymentSuccess: suspend (method: String, amount: String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedMethod by remember { mutableStateOf("UPI") }
    var upiId by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    val booking by repository.observeBooking(bookingId).collectAsState(initial = null)

    val displayCreator = booking?.creatorName ?: creatorName
    val totalAmount = parseRupees(booking?.packagePrice ?: "15000")
    val advanceAmount = if (totalAmount > 0) (totalAmount * 0.2).toInt() else 3000

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(onClick = { onBackClick() }) {
            Text("← Back")
        }

        Text(text = "💳 Payment Details", fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(text = "For $displayCreator - $eventType", color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Booking Summary", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Creator:")
                    Text(displayCreator, fontWeight = FontWeight.Bold)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Event:")
                    Text(eventType, fontWeight = FontWeight.Bold)
                }
                if (booking != null) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Package:")
                        Text(booking!!.packageName, fontWeight = FontWeight.Bold)
                    }
                }
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Amount:")
                    Text("₹ $totalAmount", fontWeight = FontWeight.Bold)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Advance to Pay:", color = Color(0xFF4CAF50))
                    Text("₹ $advanceAmount", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50), fontSize = 18.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Select Payment Method", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val methods = listOf("UPI", "Card", "Net Banking", "Cash on Event")
            for (method in methods) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (selectedMethod == method) CardDefaults.cardColors(Color(0xFFE8F5E9)) else CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    onClick = { selectedMethod = method }
                ) {
                    Row(modifier = Modifier.padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = if (selectedMethod == method) "✅ $method" else "○ $method",
                            fontWeight = if (selectedMethod == method) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedMethod == "UPI") {
            OutlinedTextField(
                value = upiId,
                onValueChange = { upiId = it },
                label = { Text("Enter UPI ID") },
                placeholder = { Text("Ex: anjali@okhdfc") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (selectedMethod == "Card") {
            OutlinedTextField(value = cardNumber, onValueChange = { cardNumber = it }, label = { Text("Card Number") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = expiry, onValueChange = { expiry = it }, label = { Text("MM/YY") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = cvv, onValueChange = { cvv = it }, label = { Text("CVV") }, modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                scope.launch {
                    val amount = "₹ $advanceAmount"
                    onPaymentSuccess(selectedMethod, amount)
                    Toast.makeText(context, "₹ $advanceAmount Paid via $selectedMethod", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("PAY ₹ $advanceAmount NOW", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = { onBackClick() }, modifier = Modifier.fillMaxWidth()) {
            Text("Cancel Payment")
        }
    }
}
