//package com.example.findajc.screens
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.MailOutline
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import coil.compose.AsyncImage
//import coil.compose.rememberImagePainter
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//import kotlinx.coroutines.tasks.await
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BusinessDetailsScreen(businessId: String) {
//    val auth = FirebaseAuth.getInstance()
//    val databaseReference = FirebaseDatabase.getInstance().reference
//
//    var businessDetails by remember { mutableStateOf<Map<String, Any>>(emptyMap()) }
//    var user by remember { mutableStateOf(auth.currentUser) }
//    var ratings by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
//    var rating by remember { mutableStateOf(0) }
//    var review by remember { mutableStateOf("") }
//    var showImages by remember { mutableStateOf(false) }
//    var showMaps by remember { mutableStateOf(false) }
//    var showReviews by remember { mutableStateOf(false) }
//    var isMessengerModalOpen by remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        fetchBusinessDetails(databaseReference, businessId) {
//            businessDetails = it
//            // Increment view count
//            val currentViews = (it["views"] as? Int) ?: 0
//            databaseReference.child("businesses/$businessId").updateChildren(mapOf("views" to (currentViews + 1)))
//        }
//
//        fetchRatings(databaseReference, businessId) {
//            ratings = it
//        }
//
//        auth.addAuthStateListener { firebaseUser ->
//            user = firebaseUser
//        }
//    }
//
//    Scaffold(
//        backgroundColor = Color(0xFFE3F2FD),
//        topBar = {
//            TopAppBar(
//                title = { Text(text = businessDetails["businessName"]?.toString() ?: "Business Details") },
//                backgroundColor = Color(0xFFE3F2FD),
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = {
//                if (user == null) {
//                    // Handle Google Sign In
//                } else {
//                    isMessengerModalOpen = true
//                }
//            }) {
//                Icon(imageVector = Icons.Filled.MailOutline, contentDescription = "Messenger")
//            }
//        },
//        bottomBar = {
//            if (isMessengerModalOpen) {
//                BottomSheet(
//                    onDismissRequest = { isMessengerModalOpen = false }
//                ) {
//                    Column(
//                        modifier = Modifier.padding(16.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text("Messaging Feature Coming Soon", fontSize = 18.sp, fontWeight = FontWeight.Bold)
//                        Spacer(modifier = Modifier.height(16.dp))
//                        Button(onClick = { isMessengerModalOpen = false }) {
//                            Text("Close")
//                        }
//                    }
//                }
//            }
//        }
//    ) { paddingValues ->
//        LazyColumn(
//            contentPadding = paddingValues,
//            modifier = Modifier.fillMaxSize()
//        ) {
//            item {
//                // About Us Card
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                        .background(Color.White)
//                ) {
//                    Column(
//                        modifier = Modifier.padding(16.dp)
//                    ) {
//                        Row {
//                            // Logo
//                            AsyncImage(
//                                model = (businessDetails["logo"].toString()),
//                                contentDescription = "Business Logo",
//                                modifier = Modifier
//                                    .size(80.dp)
//                                    .clip(CircleShape)
//                                    .background(Color.Gray)
//                            )
//                            Spacer(modifier = Modifier.width(16.dp))
//                            Column {
//                                Text(
//                                    text = businessDetails["businessName"].toString(),
//                                    fontSize = 24.sp,
//                                    fontWeight = FontWeight.Bold
//                                )
//                                Text(
//                                    text = "Operates in ${businessDetails["county"]}, ${businessDetails["location"]}",
//                                    fontSize = 16.sp
//                                )
//                                Text(
//                                    text = "Phone: ${businessDetails["mobileNumber"]}",
//                                    fontSize = 16.sp
//                                )
//                            }
//                        }
//                        Spacer(modifier = Modifier.height(16.dp))
//                        Text(
//                            text = businessDetails["description"].toString(),
//                            fontSize = 16.sp
//                        )
//                    }
//                }
//            }
//            item {
//                // Toggle Images and Maps Buttons
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Button(onClick = { showImages = !showImages }) {
//                        Text(if (showImages) "Hide Images" else "Show Images")
//                    }
//                    Button(onClick = { showMaps = !showMaps }) {
//                        Text(if (showMaps) "Hide Location" else "Show Location")
//                    }
//                }
//            }
//            item {
//                // Images Card
//                if (showImages) {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        Column(
//                            modifier = Modifier.padding(16.dp)
//                        ) {
//                            Text(
//                                text = "Images",
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                            // Grid of Images
//                            LazyVerticalGrid(
//                                columns = GridCells.Fixed(2),
//                                contentPadding = PaddingValues(4.dp),
//                                modifier = Modifier.fillMaxWidth()
//                            ) {
//                                items(businessDetails["photos"] as List<*>) { photo ->
//                                    Image(
//                                        painter = rememberImagePainter(photo),
//                                        contentDescription = "Business Image",
//                                        modifier = Modifier
//                                            .size(150.dp)
//                                            .clip(RoundedCornerShape(8.dp))
//                                            .clickable {
//                                                // Handle image click
//                                            }
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            item {
//                // Map Card
//                if (showMaps) {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        Column(
//                            modifier = Modifier.padding(16.dp)
//                        ) {
//                            Text(
//                                text = "Map",
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                            Spacer(modifier = Modifier.height(16.dp))
//                            // Map Component
//                            // Implement GoogleMapView here
//                        }
//                    }
//                }
//            }
//            item {
//                // Services and Products Card
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                ) {
//                    Column(
//                        modifier = Modifier.padding(16.dp)
//                    ) {
//                        Text(
//                            text = "Services and Products",
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))
//                        businessDetails["products"]?.let {
//                            it.forEach { product ->
//                                Text(text = "- $product")
//                            }
//                        }
//                    }
//                }
//            }
//            item {
//                // Active Working Hours Card
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                ) {
//                    Column(
//                        modifier = Modifier.padding(16.dp)
//                    ) {
//                        Text(
//                            text = "Active Working Hours",
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Text(text = businessDetails["activeHours"].toString())
//                    }
//                }
//            }
//            item {
//                // Ratings Section
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                ) {
//                    Column(
//                        modifier = Modifier.padding(16.dp)
//                    ) {
//                        Text(
//                            text = "Rate the Business",
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                        // Rating Stars
//                        Row {
//                            for (i in 1..5) {
//                                IconButton(onClick = { rating = i }) {
//                                    Icon(
//                                        imageVector = Icons.Filled.Star,
//                                        contentDescription = "Rating Star",
//                                        tint = if (i <= rating) Color.Yellow else Color.Gray
//                                    )
//                                }
//                            }
//                        }
//                        Spacer(modifier = Modifier.height(8.dp))
//                        TextField(
//                            value = review,
//                            onValueChange = { review = it },
//                            label = { Text("Write a Review") },
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Button(
//                            onClick = {
//                                // Submit Rating and Review
//                                if (user != null) {
//                                    val ratingData = mapOf(
//                                        "rating" to rating,
//                                        "review" to review,
//                                        "userId" to user!!.uid
//                                    )
//                                    databaseReference.child("ratings/$businessId").push().setValue(ratingData)
//                                }
//                            }
//                        ) {
//                            Text("Submit Rating")
//                        }
//                    }
//                }
//            }
//            item {
//                // Reviews List
//                if (showReviews) {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        Column(
//                            modifier = Modifier.padding(16.dp)
//                        ) {
//                            Text(
//                                text = "Reviews",
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            LazyColumn {
//                                items(ratings) { rating ->
//                                    Column {
//                                        Text(
//                                            text = "Rating: ${rating["rating"]}",
//                                            fontSize = 16.sp,
//                                            fontWeight = FontWeight.Bold
//                                        )
//                                        Text(
//                                            text = "Review: ${rating["review"]}",
//                                            fontSize = 16.sp
//                                        )
//                                        Spacer(modifier = Modifier.height(8.dp))
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//// Helper functions
//private suspend fun fetchBusinessDetails(databaseReference: DatabaseReference, businessId: String, onResult: (Map<String, Any>) -> Unit) {
//    val snapshot = databaseReference.child("businesses/$businessId").get().await()
//    onResult(snapshot.value as? Map<String, Any> ?: emptyMap())
//}
//
//private suspend fun fetchRatings(databaseReference: DatabaseReference, businessId: String, onResult: (List<Map<String, Any>>) -> Unit) {
//    val snapshot = databaseReference.child("ratings/$businessId").get().await()
//    val ratingsList = (snapshot.value as? List<Map<String, Any>>) ?: emptyList()
//    onResult(ratingsList)
//}
