package com.anjali.wedmoments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.anjali.wedmoments.data.GalleryItemEntity

@Composable
fun GalleryScreen(
    creator: CreatorEntity,
    repository: AppRepository,
    onBackClick: () -> Unit,
    onImageClick: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf("Photos") }
    var items by remember { mutableStateOf<List<GalleryItemEntity>>(emptyList()) }

    LaunchedEffect(creator.id, selectedTab) {
        items = repository.getGallery(creator.id, selectedTab)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("← Back")
        }

        Text(text = "Gallery", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(text = creator.name, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(selected = selectedTab == "Photos", onClick = { selectedTab = "Photos" }, label = { Text("📸 Photos") })
            FilterChip(selected = selectedTab == "Videos", onClick = { selectedTab = "Videos" }, label = { Text("🎥 Videos") })
            FilterChip(selected = selectedTab == "Reels", onClick = { selectedTab = "Reels" }, label = { Text("🎬 Reels") })
        }

        Spacer(modifier = Modifier.height(16.dp))

        val columns = when (selectedTab) {
            "Videos" -> 2
            else -> 3
        }
        val ratio = when (selectedTab) {
            "Videos" -> 16f / 9f
            "Reels" -> 9f / 16f
            else -> 1f
        }
        val color = when (selectedTab) {
            "Videos" -> Color(0xFF212121)
            "Reels" -> Color(0xFFE1BEE7)
            else -> Color(0xFFF5F5F5)
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items, key = { it.id }) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth().aspectRatio(ratio),
                    colors = CardDefaults.cardColors(containerColor = color),
                    elevation = CardDefaults.cardElevation(2.dp),
                    onClick = { onImageClick(item.title) }
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = item.emoji, fontSize = 32.sp)
                    }
                }
            }
        }
    }
}
