package com.example.findajc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.findajc.pages.HomeContent
import com.example.findajc.pages.JobsPage
import com.example.findajc.pages.JoinPage
import com.example.findajc.pages.MorePage
import com.example.findajc.ui.theme.FindaJcTheme
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}
        setContent {
            FindaJcTheme {
                HomePage()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Join", "Jobs", "More")
    val icons = listOf(
        Icons.Filled.Home,
        Icons.Filled.Person,
        Icons.Filled.Build,
        Icons.Filled.MoreVert
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
//        topBar = {
//            TopAppBar(
//                modifier = Modifier.padding(8.dp),// colors = MaterialTheme.colorScheme.surface,
//                title = { Text("App Title") }
//            )
//        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp).clip(RoundedCornerShape(16.dp)),
                tonalElevation = 8.dp
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = item
                            )
                        },
                        label = { Text(item) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedIndex) {
            0 -> HomeContent(Modifier.padding(innerPadding))
            1 -> JoinPage(Modifier.padding(innerPadding))
            2 -> JobsPage(Modifier.padding(innerPadding))
            3 -> MorePage(Modifier.padding(innerPadding))
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    FindaJcTheme {
        HomePage()
    }
}
