package com.anjali.wedmoments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CreatorProfileScreen(
    creatorName: String,
    onBackClick: () -> Unit,
    onBookClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onReviewsClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // =====================================
        // BACK BUTTON
        // =====================================

        TextButton(
            onClick = {

                onBackClick()

            }
        ) {

            Text("← Back")

        }

        // =====================================
        // CREATOR PROFILE CARD
        // =====================================

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Row {

                    Text(
                        text = "👤",
                        fontSize = 60.sp
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {

                        Text(
                            text = creatorName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Experience: 5+ Years",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "⭐ 4.9 (200+ Reviews)",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "📍 Mumbai, Maharashtra",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Instagram: @${creatorName.replace(" ", "").lowercase()}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF2196F3)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // =====================================
        // PORTFOLIO SECTION
        // =====================================

        Text(
            text = "Portfolio",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "📸 500+ Photos | 🎥 100+ Videos | 🎬 50+ Reels",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Speciality: Candid, Traditional, Pre-Wedding",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // =====================================
        // REVIEWS SECTION - SHORT
        // =====================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ) {

            Text(
                text = "Reviews",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            TextButton(
                onClick = {

                    onReviewsClick()

                }
            ) {

                Text("View All")

            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {

            Column(
                modifier = Modifier.padding(12.dp)
            ) {

                Text(
                    text = "⭐⭐⭐⭐⭐ - Priya & Rohan",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Amazing work! Loved our photos",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {

            Column(
                modifier = Modifier.padding(12.dp)
            ) {

                Text(
                    text = "⭐⭐⭐⭐⭐ - Sneha & Amit",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Best photographer in Mumbai!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // =====================================
        // ALL BUTTONS
        // =====================================

        OutlinedButton(
            onClick = {

                onGalleryClick()

            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("VIEW GALLERY")

        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = {

                onReviewsClick()

            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("⭐ VIEW ALL REVIEWS (200+)")

        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {

                onBookClick()

            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("VIEW PACKAGES")

        }
    }
}