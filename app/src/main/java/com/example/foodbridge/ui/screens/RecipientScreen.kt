package com.example.foodbridge.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun RecipientScreen(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(0) }
    
    val scheduledDeliveries = remember {
        listOf(
            Donation(
                id = "1",
                foodType = "Biriyani",
                quantity = 25,
                description = "Fresh biriyani from restaurant",
                location = Location(address = "Dhaka, Banani"),
                expiryTime = System.currentTimeMillis() + 3600000,
                status = DonationStatus.PICKED_UP,
                donorName = "Restaurant XYZ"
            ),
            Donation(
                id = "2",
                foodType = "Mixed Vegetables",
                quantity = 15,
                description = "Prepared vegetables from hotel",
                location = Location(address = "Dhaka, Gulshan"),
                expiryTime = System.currentTimeMillis() + 7200000,
                status = DonationStatus.CLAIMED,
                donorName = "Hotel ABC"
            )
        )
    }
    
    val deliveryHistory = remember {
        listOf(
            Donation(
                id = "3",
                foodType = "Rice & Curry",
                quantity = 30,
                description = "Home-cooked meal",
                location = Location(address = "Dhaka, Dhanmondi"),
                expiryTime = System.currentTimeMillis() - 86400000,
                status = DonationStatus.DELIVERED,
                donorName = "Household"
            ),
            Donation(
                id = "4",
                foodType = "Chicken Curry",
                quantity = 20,
                description = "Restaurant leftovers",
                location = Location(address = "Dhaka, Mirpur"),
                expiryTime = System.currentTimeMillis() - 172800000,
                status = DonationStatus.DELIVERED,
                donorName = "Restaurant DEF"
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipient Dashboard", fontWeight = FontWeight.Bold) },
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
                    // Quick stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "Meals Received",
                            value = "42",
                            icon = Icons.Default.Restaurant,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "This Month",
                            value = "8",
                            icon = Icons.Default.CalendarMonth,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                item {
                    // Upcoming delivery banner
                    if (scheduledDeliveries.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Upcoming Delivery",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${scheduledDeliveries[0].foodType} - ${scheduledDeliveries[0].quantity} servings",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Estimated arrival: 25 min",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                IconButton(onClick = { /* Track delivery */ }) {
                                    Icon(
                                        Icons.Default.MyLocation,
                                        "Track",
                                        modifier = Modifier.size(32.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your Deliveries",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                item {
                    TabRow(selectedTabIndex = selectedTab) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Scheduled (${scheduledDeliveries.size})") }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("History (${deliveryHistory.size})") }
                        )
                    }
                }
                
                items(
                    when (selectedTab) {
                        0 -> scheduledDeliveries
                        else -> deliveryHistory
                    }
                ) { donation ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = donation.foodType,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                if (donation.status == DonationStatus.PICKED_UP) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.LocalShipping,
                                                null,
                                                modifier = Modifier.size(16.dp),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                "On the way",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = donation.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Quantity",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${donation.quantity} servings",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Column {
                                    Text(
                                        text = "From",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = donation.donorName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            
                            if (selectedTab == 0 && donation.status == DonationStatus.PICKED_UP) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = { /* Track delivery */ },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.MyLocation, null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Track Delivery")
                                }
                            }
                            
                            if (selectedTab == 1) {
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedButton(
                                    onClick = { /* Rate delivery */ },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Star, null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Rate & Review")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

