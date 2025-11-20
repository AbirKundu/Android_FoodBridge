package com.example.foodbridge.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.foodbridge.model.Donation
import com.example.foodbridge.model.DonationStatus
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DonationCard(
    donation: Donation,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onCardClick,
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
                StatusChip(status = donation.status)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = donation.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    label = "Quantity",
                    value = "${donation.quantity} servings"
                )
                InfoItem(
                    label = "Location",
                    value = donation.location.address.takeIf { it.isNotEmpty() } ?: "Nearby"
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Expires: ${formatTime(donation.expiryTime)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun StatusChip(status: DonationStatus) {
    val (text, color) = when (status) {
        DonationStatus.AVAILABLE -> "Available" to MaterialTheme.colorScheme.primary
        DonationStatus.CLAIMED -> "Claimed" to MaterialTheme.colorScheme.tertiary
        DonationStatus.PICKED_UP -> "Picked Up" to MaterialTheme.colorScheme.secondary
        DonationStatus.DELIVERED -> "Delivered" to MaterialTheme.colorScheme.primary
        DonationStatus.EXPIRED -> "Expired" to MaterialTheme.colorScheme.error
        DonationStatus.CANCELLED -> "Cancelled" to MaterialTheme.colorScheme.error
    }
    
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

