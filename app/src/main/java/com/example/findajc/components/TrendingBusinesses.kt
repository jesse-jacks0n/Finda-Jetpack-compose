package com.example.findajc.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.findajc.HomePage
import com.example.findajc.R
import com.example.findajc.ui.theme.FindaJcTheme
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

@Composable
fun TrendingBusinesses() {
    var businesses by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val databaseReference = FirebaseDatabase.getInstance().getReference("businesses")

    LaunchedEffect(Unit) {
        try {
            val snapshot = databaseReference.limitToFirst(3).get().await()
            val loadedBusinesses = snapshot.children.mapNotNull { dataSnapshot ->
                val key = dataSnapshot.key ?: return@mapNotNull null
                val value = dataSnapshot.value as? Map<*, *> ?: return@mapNotNull null
                val businessName = value["businessName"] as? String ?: return@mapNotNull null
                val logo = value["logo"] as? String ?: ""
                val location = value["location"] as? String ?: ""
                val description = value["description"] as? String ?: ""

                // Calculate average rating
                val ratings = value["ratings"] as? Map<*, *> ?: return@mapNotNull null
                val averageRating = ratings.values
                    .filterIsInstance<Map<*, *>>()
                    .mapNotNull { it["rating"] as? Number }
                    .map { it.toDouble() }
                    .average()

                mapOf(
                    "id" to key,
                    "businessName" to businessName,
                    "logo" to logo,
                    "location" to location,
                    "description" to description,
                    "averageRating" to averageRating
                )
            }
            businesses = loadedBusinesses
        } catch (e: Exception) {
            Log.e("TrendingBusinesses", "Error fetching businesses", e)
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Trending Businesses in Kenya",
            style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "BOSA 2023 Top 3 Award-winners overall",
            style = TextStyle(fontSize = 14.sp, color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            // Show skeleton loaders
            repeat(3) {
                SkeletonLoader(modifier = Modifier
                    .padding(vertical = 5.dp)
                    .fillMaxWidth()
                )
            }
        } else {
            // Show actual businesses
            businesses.forEach { business ->
                TrendingBusinessCard(business)
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

@Composable
fun SkeletonLoader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color.Gray, shape = RoundedCornerShape(16.dp))
            .height(100.dp) // Adjust size as needed
            .fillMaxWidth()
    )
}

@Composable
fun TrendingBusinessCard(business: Map<String, Any>) {
    val id = business["id"] as? String ?: "No ID"
    val businessName = business["businessName"] as? String ?: "No Name"
    val logo = business["logo"] as? String ?: ""
    val description = business["description"] as? String ?: "No Description"
    val averageRating = business["averageRating"] as? Double ?: 0.0

    Log.d("TrendingBusinessCard", "Displaying card for: $businessName") // Log card info

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navigate to business details */ }
            .padding(10.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.padding(5.dp)) {
                AsyncImage(
                    model = logo,
                    contentDescription = "Business Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = businessName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = averageRating.toString().take(1),
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

