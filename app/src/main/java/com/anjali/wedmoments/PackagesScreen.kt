package com.anjali.wedmoments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.anjali.wedmoments.data.PackageEntity

@Composable
fun PackagesScreen(
    creator: CreatorEntity,
    repository: AppRepository,
    onBackClick: () -> Unit,
    onPackageSelected: (PackageEntity) -> Unit
) {
    var packages by remember { mutableStateOf<List<PackageEntity>>(emptyList()) }

    LaunchedEffect(creator.id) {
        packages = repository.getPackages(creator.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("← Back")
        }

        Text(text = "Packages", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = creator.name, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(20.dp))

        if (packages.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "📦", fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "No packages available", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            packages.forEach { pkg ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = pkg.name, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = pkg.price, style = MaterialTheme.typography.titleMedium, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "📸 ${pkg.photos}")
                        Text(text = "🎬 ${pkg.reels}")
                        Text(text = "🎥 ${pkg.videos}")
                        Text(text = "⏱ ${pkg.hours}")
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { onPackageSelected(pkg) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("SELECT ${pkg.name}")
                        }
                    }
                }
            }
        }
    }
}
