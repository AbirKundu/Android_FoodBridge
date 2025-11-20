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
fun VolunteerScreen(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(0) }
    
    val availableDonations = remember {
        listOf(
            Donation(
                id = "1",
                foodType = "Rice & Curry",
                quantity = 30,
                description = "Home-cooked meal surplus",
                location = Location(address = "Dhaka, Dhanmondi"),
                expiryTime = System.currentTimeMillis() + 5400000,
                status = DonationStatus.AVAILABLE
            ),
            Donation(
                id = "2",
                foodType = "Chicken Curry",
                quantity = 20,
                description = "Restaurant leftovers",
                location = Location(address = "Dhaka, Mirpur"),
                expiryTime = System.currentTimeMillis() + 3600000,
                status = DonationStatus.AVAILABLE
            ),
            Donation(
                id = "3",
                foodType = "Vegetable Platter",
                quantity = 15,
                description = "From hotel kitchen",
                location = Location(address = "Dhaka, Uttara"),
                expiryTime = System.currentTimeMillis() + 7200000,
                status = DonationStatus.AVAILABLE
            )
        )
    }
    
    val claimedDonations = remember {
        listOf(
            Donation(
                id = "4",
                foodType = "Biriyani",
                quantity = 25,
                description = "Fresh biriyani from restaurant",
                location = Location(address = "Dhaka, Banani"),
                expiryTime = System.currentTimeMillis() + 3600000,
                status = DonationStatus.CLAIMED
            ),
            Donation(
                id = "5",
                foodType = "Mixed Vegetables",
                quantity = 15,
                description = "Prepared vegetables",
                location = Location(address = "Dhaka, Gulshan"),
                expiryTime = System.currentTimeMillis() + 7200000,
                status = DonationStatus.PICKED_UP
            )
        )
    }
    
    val volunteerStats = remember {
        ImpactStats(
            mealsDelivered = 89,
            kgWasteDiverted = 32.1,
            co2Saved = 8.9,
            totalDonations = 15
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Volunteer Dashboard", fontWeight = FontWeight.Bold) },
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
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
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
                            title = "Deliveries",
                            value = "${volunteerStats.totalDonations}",
                            icon = Icons.Default.LocalShipping,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Meals Delivered",
                            value = "${volunteerStats.mealsDelivered}",
                            icon = Icons.Default.Restaurant,
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
                            title = "Distance Traveled",
                            value = "124 km",
                            icon = Icons.Default.DirectionsCar,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Rating",
                            value = "4.8",
                            icon = Icons.Default.Star,
                            modifier = Modifier.weight(1f),
                            subtitle = "From 15 reviews"
                        )
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Available Donations",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                item {
                    TabRow(selectedTabIndex = selectedTab) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Available (${availableDonations.size})") }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("My Claims (${claimedDonations.size})") }
                        )
                    }
                }
                
                items(
                    when (selectedTab) {
                        0 -> availableDonations
                        else -> claimedDonations
                    }
                ) { donation ->
                    DonationCard(
                        donation = donation,
                        onCardClick = { /* Navigate to details */ }
                    )
                    
                    if (selectedTab == 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { /* Claim donation */ },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Claim")
                            }
                            OutlinedButton(
                                onClick = { /* View on map */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Map, null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Map")
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { /* Mark picked up */ },
                                modifier = Modifier.weight(1f),
                                enabled = donation.status == DonationStatus.CLAIMED
                            ) {
                                Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Picked Up")
                            }
                            OutlinedButton(
                                onClick = { /* Track delivery */ },
                                modifier = Modifier.weight(1f),
                                enabled = donation.status == DonationStatus.PICKED_UP
                            ) {
                                Icon(Icons.Default.MyLocation, null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Track")
                            }
                        }
                    }
                }
            }
        }
    }
}

