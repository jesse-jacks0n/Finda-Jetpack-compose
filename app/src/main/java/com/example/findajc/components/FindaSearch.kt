package com.example.findajc.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindaSearch() {
    var searchQuery by remember { mutableStateOf("") }
    var counties by remember { mutableStateOf(listOf<Map<String, String>>()) }
    var filteredCounties by remember { mutableStateOf(listOf<Map<String, String>>()) }
    var showDropdown by remember { mutableStateOf(false) }
    val databaseReference = FirebaseDatabase.getInstance().getReference("county")
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        fetchCounties(databaseReference) { loadedCounties ->
            counties = loadedCounties
            filteredCounties = loadedCounties
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                showDropdown = query.isNotEmpty()
                filteredCounties = counties.filter { it["name"]?.contains(query, ignoreCase = true) == true }
            },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon") },
            placeholder = { Text("Search County...") },
            shape = CircleShape,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = Color.Cyan,
                unfocusedBorderColor = Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        if (showDropdown) {
            DropdownMenu(
                expanded = showDropdown,
                onDismissRequest = { showDropdown = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                filteredCounties.forEach { county ->
                    DropdownMenuItem(
                        onClick = {
                            showDropdown = false
                            searchQuery = county["name"].orEmpty()
                            handleCountySelected(county["id"].orEmpty())
                        },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "County Icon")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = county["name"].orEmpty(), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    )
                }
            }
        }
    }
}

private fun handleCountySelected(countyId: String) {
    // Logic to handle county selection. For example:
    // - Navigate to another screen
    // - Display additional information
    // - Log the selection
    Log.d("FindaSearch", "County selected: $countyId")
}

private fun fetchCounties(databaseReference: DatabaseReference, onCountiesLoaded: (List<Map<String, String>>) -> Unit) {
    databaseReference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val loadedCounties = snapshot.children.mapNotNull { dataSnapshot ->
                val key = dataSnapshot.key ?: return@mapNotNull null
                val name = dataSnapshot.child("name").getValue(String::class.java) ?: return@mapNotNull null
                mapOf("id" to key, "name" to name)
            }
            onCountiesLoaded(loadedCounties)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("FindaSearch", "DatabaseError: ${error.message}")
        }
    })
}

