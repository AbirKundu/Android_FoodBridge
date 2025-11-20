package com.example.foodbridge.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodbridge.ui.theme.AccentOrange
import com.example.foodbridge.ui.theme.PrimaryGreen
import com.example.foodbridge.ui.theme.SecondaryGreen

@Composable
fun RoleSelectionScreen(
    onRoleSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        PrimaryGreen.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo/Icon with animation
            Icon(
                imageVector = Icons.Default.Restaurant,
                contentDescription = "FoodBridge",
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale),
                tint = PrimaryGreen
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "FoodBridge",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Connecting Surplus Food with Those in Need",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "Select Your Role",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            RoleCard(
                role = "Donor",
                description = "Restaurants, Hotels, Households",
                icon = Icons.Default.RestaurantMenu,
                color = PrimaryGreen,
                onClick = { onRoleSelected("Donor") }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            RoleCard(
                role = "Volunteer",
                description = "Help deliver food to recipients",
                icon = Icons.Default.VolunteerActivism,
                color = AccentOrange,
                onClick = { onRoleSelected("Volunteer") }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            RoleCard(
                role = "Recipient",
                description = "Receive food donations",
                icon = Icons.Default.FamilyRestroom,
                color = SecondaryGreen,
                onClick = { onRoleSelected("Recipient") }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            RoleCard(
                role = "Admin",
                description = "Manage platform and users",
                icon = Icons.Default.AdminPanelSettings,
                color = MaterialTheme.colorScheme.tertiary,
                onClick = { onRoleSelected("Admin") }
            )
        }
    }
}

@Composable
fun RoleCard(
    role: String,
    description: String,
    icon: ImageVector,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(16.dp),
                color = color.copy(alpha = 0.2f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = role,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxSize(),
                    tint = color
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = role,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

