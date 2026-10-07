package com.anjali.wedmoments

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anjali.wedmoments.data.AppRepository
import com.anjali.wedmoments.data.CreatorEntity
import com.anjali.wedmoments.data.GalleryItemEntity

@Composable
fun CreatorPortfolioScreen(
    creator: CreatorEntity,
    repository: AppRepository,
    onBackClick: () -> Unit,
    onBookClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Photos") }
    val context = LocalContext.current
    var gallery by remember { mutableStateOf<List<GalleryItemEntity>>(emptyList()) }
    val reviews by repository.observeReviews(creator.id).collectAsState(initial = emptyList())

    LaunchedEffect(creator.id, selectedTab) {
        if (selectedTab != "Reviews") {
            gallery = repository.getGallery(creator.id, selectedTab)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(onClick = { onBackClick() }) { Text("← Back") }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "👤 ${creator.name}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = "📷 Instagram @${creator.instagram}", color = Color(0xFF2196F3))
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val uri = Uri.parse("https://www.instagram.com/${creator.instagram}")
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("📸 Open Instagram Profile") }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { selectedTab = "Photos" }, modifier = Modifier.weight(1f)) { Text("Photos") }
            OutlinedButton(onClick = { selectedTab = "Videos" }, modifier = Modifier.weight(1f)) { Text("Videos") }
            OutlinedButton(onClick = { selectedTab = "Reviews" }, modifier = Modifier.weight(1f)) { Text("Reviews") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == "Photos" || selectedTab == "Videos") {
            Text(text = if (selectedTab == "Photos") "📸 Photos (${gallery.size})" else "🎥 Videos (${gallery.size})", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                gallery.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (selectedTab == "Videos") 120.dp else 100.dp)
                            .clickable {
                                if (selectedTab == "Videos") {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/")))
                                }
                                Toast.makeText(context, "${item.title} opened", Toast.LENGTH_SHORT).show()
                            },
                        colors = CardDefaults.cardColors(if (selectedTab == "Videos") Color.Black else Color(0xFFE0E0E0))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${item.emoji} ${item.title}",
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == "Videos") Color.White else Color.Unspecified
                            )
                        }
                    }
                }
            }
        }

        if (selectedTab == "Reviews") {
            Text(text = "⭐ Reviews (${reviews.size})", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                reviews.forEach { review ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "${"⭐".repeat(review.rating)} ${review.authorName}", fontWeight = FontWeight.Bold)
                            Text(text = review.comment)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { onBookClick() }, modifier = Modifier.fillMaxWidth()) {
            Text("BOOK NOW")
        }
    }
}
