package com.example.foodbridge.model

import java.util.Date

data class Donation(
    val id: String = "",
    val donorId: String = "",
    val donorName: String = "",
    val foodType: String = "",
    val quantity: Int = 0,
    val description: String = "",
    val location: Location = Location(),
    val expiryTime: Long = 0L,
    val status: DonationStatus = DonationStatus.AVAILABLE,
    val createdAt: Long = System.currentTimeMillis(),
    val imageUrl: String = "",
    val claimedBy: String? = null,
    val deliveredTo: String? = null
)

data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = ""
)

enum class DonationStatus {
    AVAILABLE,
    CLAIMED,
    PICKED_UP,
    DELIVERED,
    EXPIRED,
    CANCELLED
}

data class ImpactStats(
    val mealsDelivered: Int = 0,
    val kgWasteDiverted: Double = 0.0,
    val co2Saved: Double = 0.0,
    val activeVolunteers: Int = 0,
    val totalDonations: Int = 0
)

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: UserRole = UserRole.DONOR,
    val phone: String = "",
    val location: Location = Location(),
    val verified: Boolean = false,
    val rating: Double = 0.0,
    val totalDeliveries: Int = 0
)

enum class UserRole {
    DONOR,
    VOLUNTEER,
    RECIPIENT,
    ADMIN
}

