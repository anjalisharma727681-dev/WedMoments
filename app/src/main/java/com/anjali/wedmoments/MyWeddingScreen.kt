package com.anjali.wedmoments

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.anjali.wedmoments.data.WeddingTaskEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun MyWeddingScreen(
    repository: AppRepository,
    userId: Long,
    creatorName: String?,
    onBackClick: () -> Unit,
    onToggleTask: (WeddingTaskEntity) -> Unit
) {
    val wedding by repository.observeWedding(userId).collectAsState(initial = null)
    val tasks by repository.observeTasks(wedding?.id ?: 0).collectAsState(initial = emptyList())
    val remaining = remainingTime(wedding?.date.orEmpty())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(onClick = { onBackClick() }) {
            Text("← Back")
        }

        Text("💍 My Wedding", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Countdown + Details", color = Color.Gray)
        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(Color(0xFFE91E63))
        ) {
            Column(
                Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(wedding?.date ?: "Date not set", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    CountCard(remaining.first.toString(), "Days")
                    CountCard(remaining.second.toString(), "Hours")
                    CountCard(remaining.third.toString(), "Mins")
                }
                Spacer(Modifier.height(8.dp))
                Text("with ${creatorName ?: wedding?.photographer.orEmpty()}", color = Color.White, fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Details", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(Color.White)) {
            Column(Modifier.padding(16.dp)) {
                Text("👰 Bride: ${wedding?.brideName.orEmpty()}")
                Text("🤵 Groom: ${wedding?.groomName.orEmpty()}")
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                Text("📅 Date: ${wedding?.date.orEmpty()}")
                Text("📍 Venue: ${wedding?.venue.orEmpty()}")
                Text("📸 Photographer: ${wedding?.photographer.orEmpty()}")
                Text("💰 Package: ${wedding?.packageName.orEmpty()}")
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Tasks", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(Color.White)) {
            Column(Modifier.padding(12.dp)) {
                tasks.forEach { task ->
                    TextButton(onClick = { onToggleTask(task) }) {
                        Text(if (task.isDone) "✅ ${task.title}" else "⬜ ${task.title}")
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Button(onClick = { onBackClick() }, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Home")
        }
    }
}

@Composable
private fun CountCard(value: String, label: String) {
    Card(colors = CardDefaults.cardColors(Color.White)) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontWeight = FontWeight.Bold, color = Color(0xFFE91E63))
            Text(label, fontSize = 12.sp)
        }
    }
}

private fun remainingTime(dateText: String): Triple<Long, Long, Long> {
    val formats = listOf("dd/MM/yyyy", "d MMM yyyy", "dd MMM yyyy")
    val parsed = formats.firstNotNullOfOrNull { pattern ->
        runCatching { SimpleDateFormat(pattern, Locale.ENGLISH).parse(dateText) }.getOrNull()
    } ?: return Triple(0, 0, 0)
    val diff = parsed.time - Calendar.getInstance().timeInMillis
    if (diff <= 0) return Triple(0, 0, 0)
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff) % 24
    val mins = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
    return Triple(days, hours, mins)
}
