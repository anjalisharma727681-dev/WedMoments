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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.anjali.wedmoments.data.CreatorEntity
import kotlinx.coroutines.launch

@Composable
fun ReviewsScreen(
    creator: CreatorEntity,
    repository: AppRepository,
    userId: Long,
    onBackClick: () -> Unit
) {
    var reviewText by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(5) }
    val reviewsList by repository.observeReviews(creator.id).collectAsState(initial = emptyList())
    val user by repository.observeUser(userId).collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val average = if (reviewsList.isEmpty()) 0.0 else reviewsList.map { it.rating }.average()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextButton(onClick = { onBackClick() }) {
            Text("← Back")
        }

        Text(text = "⭐ Reviews", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(text = creator.name, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = String.format("%.1f", average), fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Text(text = "⭐".repeat(average.toInt().coerceIn(0, 5)), fontSize = 16.sp)
                    Text(text = "${reviewsList.size} Reviews", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    (5 downTo 1).forEach { star ->
                        val count = reviewsList.count { it.rating == star }
                        Text(text = "$star ⭐  $count", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Add Your Review", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (1..5).forEach { star ->
                TextButton(onClick = { rating = star }) {
                    Text(text = if (star <= rating) "⭐" else "☆", fontSize = 24.sp)
                }
            }
        }

        OutlinedTextField(
            value = reviewText,
            onValueChange = { reviewText = it },
            label = { Text("Write your review...") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (reviewText.isBlank()) {
                    Toast.makeText(context, "Write a review first", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                scope.launch {
                    repository.addReview(
                        creatorId = creator.id,
                        userId = userId,
                        authorName = user?.name ?: "Guest",
                        comment = reviewText,
                        rating = rating
                    )
                    reviewText = ""
                    Toast.makeText(context, "Review saved", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Review")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "All Reviews", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(reviewsList, key = { it.id }) { review ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = review.authorName, fontWeight = FontWeight.Bold)
                        Text(text = "⭐".repeat(review.rating), fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = review.comment, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
