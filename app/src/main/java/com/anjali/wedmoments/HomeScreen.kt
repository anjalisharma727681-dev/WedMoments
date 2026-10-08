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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anjali.wedmoments.data.AppRepository
import com.anjali.wedmoments.data.CreatorEntity

@Composable
fun HomeScreen(
    repository: AppRepository,
    onCreatorClick: (CreatorEntity) -> Unit,
    onMyWeddingClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val creatorsList by remember(searchText) {
        repository.observeCreators(searchText)
    }.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "WedMoments",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Find Best Creators",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Row {
                TextButton(onClick = { onMyWeddingClick() }) {
                    Text("💍", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = { onProfileClick() }) {
                    Text("👤", fontSize = 24.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search photographers, makeup...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Categories", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Card(colors = CardDefaults.cardColors(Color(0xFFFCE4EC))) {
                Text(text = "📸 Photography", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.bodySmall)
            }
            Card(colors = CardDefaults.cardColors(Color(0xFFE3F2FD))) {
                Text(text = "🎥 Videography", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.bodySmall)
            }
            Card(colors = CardDefaults.cardColors(Color(0xFFF3E5F5))) {
                Text(text = "💄 Makeup", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Top Creators Near You", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        if (creatorsList.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🔍", fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "No creators found", fontWeight = FontWeight.Bold)
                    Text(text = "Try searching with a different keyword", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(creatorsList, key = { it.id }) { creator ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(3.dp),
                        onClick = { onCreatorClick(creator) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "👤", fontSize = 40.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = creator.name, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "⭐ ${creator.rating} | 📍 ${creator.location} | 💼 ${creator.experience}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Starts from ${creator.startingPrice}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF4CAF50),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
