package com.example.foodbridge.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.foodbridge.model.*
import com.example.foodbridge.ui.components.DonationCard
import com.example.foodbridge.ui.components.StatCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorScreen(
    navController: androidx.navigation.NavController? = null,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    var showPostDonation by remember { mutableStateOf(false) }
    val donations = remember {
        mutableStateListOf(
            Donation(
                id = "1",
                foodType = "Biriyani",
                quantity = 25,
                description = "Fresh biriyani from restaurant leftovers",
                location = Location(address = "Dhaka, Banani"),
                expiryTime = System.currentTimeMillis() + 3600000,
                status = DonationStatus.DELIVERED
            ),
            Donation(
                id = "2",
                foodType = "Mixed Vegetables",
                quantity = 15,
                description = "Prepared vegetables from hotel kitchen",
                location = Location(address = "Dhaka, Gulshan"),
                expiryTime = System.currentTimeMillis() + 7200000,
                status = DonationStatus.CLAIMED
            ),
            Donation(
                id = "3",
                foodType = "Rice & Curry",
                quantity = 30,
                description = "Home-cooked meal surplus",
                location = Location(address = "Dhaka, Dhanmondi"),
                expiryTime = System.currentTimeMillis() + 5400000,
                status = DonationStatus.AVAILABLE
            )
        )
    }
    
    val impactStats = remember {
        mutableStateOf(
            ImpactStats(
                mealsDelivered = 127,
                kgWasteDiverted = 45.3,
                co2Saved = 12.5,
                totalDonations = donations.size
            )
        )
    }
    
    // Update stats when donations change
    LaunchedEffect(donations.size) {
        impactStats.value = impactStats.value.copy(
            totalDonations = donations.size
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Donor Dashboard", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, "Notifications")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Person, "Profile")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showPostDonation = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Post Donation")
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Impact Stats
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Your Impact",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "Meals Delivered",
                            value = "${impactStats.value.mealsDelivered}",
                            icon = Icons.Default.Restaurant,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Waste Diverted",
                            value = "${impactStats.value.kgWasteDiverted} kg",
                            icon = Icons.Default.Recycling,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "CO₂ Saved",
                            value = "${impactStats.value.co2Saved} kg",
                            icon = Icons.Default.Eco,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Total Donations",
                            value = "${impactStats.value.totalDonations}",
                            icon = Icons.Default.Favorite,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your Donations",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Tabs
                item {
                    TabRow(selectedTabIndex = selectedTab) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Active (${donations.count { it.status == DonationStatus.AVAILABLE || it.status == DonationStatus.CLAIMED }})") }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("History (${donations.count { it.status == DonationStatus.DELIVERED }})") }
                        )
                    }
                }
                
                // Donations List
                items(
                    when (selectedTab) {
                        0 -> donations.filter { it.status == DonationStatus.AVAILABLE || it.status == DonationStatus.CLAIMED }
                        else -> donations.filter { it.status == DonationStatus.DELIVERED }
                    }
                ) { donation ->
                    DonationCard(
                        donation = donation,
                        onCardClick = { /* Navigate to details */ }
                    )
                }
            }
        }
    }
    
    // Post Donation Screen - Full screen overlay
    if (showPostDonation) {
        PostDonationScreen(
            onDonationPosted = { newDonation ->
                donations.add(0, newDonation) // Add to beginning of list
                showPostDonation = false
            },
            onDismiss = { showPostDonation = false }
        )
    }
}

