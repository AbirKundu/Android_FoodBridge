package com.example.foodbridge.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.foodbridge.model.Donation
import com.example.foodbridge.model.DonationStatus
import com.example.foodbridge.model.Location
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDonationScreen(
    onDonationPosted: (Donation) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Form state
    var foodType by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var hoursUntilExpiry by remember { mutableStateOf("") }
    
    // Validation errors
    var foodTypeError by remember { mutableStateOf<String?>(null) }
    var quantityError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var addressError by remember { mutableStateOf<String?>(null) }
    var expiryError by remember { mutableStateOf<String?>(null) }
    
    // Dropdown options
    val foodTypes = listOf(
        "Biriyani",
        "Rice & Curry",
        "Chicken Curry",
        "Mixed Vegetables",
        "Dal & Rice",
        "Fish Curry",
        "Vegetable Platter",
        "Fried Rice",
        "Noodles",
        "Other"
    )
    var expandedFoodType by remember { mutableStateOf(false) }
    
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post New Donation", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.ArrowBack, "Back")
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Info card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Info,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Help reduce food waste by sharing surplus food with those in need",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            // Food Type Dropdown
            Column {
                Text(
                    text = "Food Type *",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = expandedFoodType,
                    onExpandedChange = { expandedFoodType = !expandedFoodType }
                ) {
                    OutlinedTextField(
                        value = foodType,
                        onValueChange = {
                            foodType = it
                            foodTypeError = null
                        },
                        label = { Text("Select or type food type") },
                        readOnly = false,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFoodType) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        isError = foodTypeError != null,
                        supportingText = foodTypeError?.let { { Text(it) } }
                    )
                    ExposedDropdownMenu(
                        expanded = expandedFoodType,
                        onDismissRequest = { expandedFoodType = false }
                    ) {
                        foodTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    foodType = type
                                    expandedFoodType = false
                                    foodTypeError = null
                                }
                            )
                        }
                    }
                }
            }
            
            // Quantity
            Column {
                Text(
                    text = "Quantity (servings) *",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = {
                        quantity = it.filter { char -> char.isDigit() }
                        quantityError = null
                    },
                    label = { Text("Number of servings") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Restaurant, null) },
                    isError = quantityError != null,
                    supportingText = quantityError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
            
            // Description
            Column {
                Text(
                    text = "Description *",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        descriptionError = null
                    },
                    label = { Text("Describe the food") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    leadingIcon = { Icon(Icons.Default.Description, null) },
                    isError = descriptionError != null,
                    supportingText = descriptionError?.let { { Text(it) } }
                )
            }
            
            // Location/Address
            Column {
                Text(
                    text = "Pickup Location *",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = {
                        address = it
                        addressError = null
                    },
                    label = { Text("Address or area name") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                    trailingIcon = {
                        IconButton(onClick = { /* Get current location */ }) {
                            Icon(Icons.Default.MyLocation, "Use current location")
                        }
                    },
                    isError = addressError != null,
                    supportingText = addressError?.let { { Text(it) } }
                )
            }
            
            // Expiry Time
            Column {
                Text(
                    text = "Hours Until Expiry *",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = hoursUntilExpiry,
                    onValueChange = {
                        hoursUntilExpiry = it.filter { char -> char.isDigit() || char == '.' }
                        expiryError = null
                    },
                    label = { Text("How many hours until food expires?") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.AccessTime, null) },
                    isError = expiryError != null,
                    supportingText = expiryError?.let { { Text(it) } } ?: run {
                        { Text("Food should be fresh and safe to consume") }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )
            }
            
            // Photo upload section (optional)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add Photo (Optional)",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Help volunteers see what they're picking up",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(onClick = { /* Open camera/gallery */ }) {
                        Icon(Icons.Default.AddAPhoto, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Photo")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Submit Button
            Button(
                onClick = {
                    // Validate form
                    var isValid = true
                    
                    if (foodType.isBlank()) {
                        foodTypeError = "Please select or enter a food type"
                        isValid = false
                    }
                    
                    if (quantity.isBlank() || quantity.toIntOrNull() == null || quantity.toInt() <= 0) {
                        quantityError = "Please enter a valid quantity (minimum 1)"
                        isValid = false
                    }
                    
                    if (description.isBlank() || description.length < 10) {
                        descriptionError = "Please provide a description (at least 10 characters)"
                        isValid = false
                    }
                    
                    if (address.isBlank()) {
                        addressError = "Please enter a pickup location"
                        isValid = false
                    }
                    
                    val hours = hoursUntilExpiry.toDoubleOrNull()
                    if (hoursUntilExpiry.isBlank() || hours == null || hours <= 0) {
                        expiryError = "Please enter valid hours until expiry"
                        isValid = false
                    }
                    
                    if (isValid) {
                        isLoading = true
                        // Simulate API call
                        coroutineScope.launch {
                            delay(1000) // Simulate network delay
                            
                            val newDonation = Donation(
                                id = UUID.randomUUID().toString(),
                                donorId = "donor_123", // Mock donor ID
                                donorName = "Your Restaurant", // Mock name
                                foodType = foodType,
                                quantity = quantity.toInt(),
                                description = description,
                                location = Location(
                                    latitude = 23.8103, // Mock coordinates (Dhaka)
                                    longitude = 90.4125,
                                    address = address
                                ),
                                expiryTime = System.currentTimeMillis() + ((hours?.times(3600000))?.toLong()
                                    ?: 0),
                                status = DonationStatus.AVAILABLE,
                                createdAt = System.currentTimeMillis()
                            )
                            
                            isLoading = false
                            showSuccessDialog = true
                            onDonationPosted(newDonation)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Posting...")
                } else {
                    Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Post Donation", fontWeight = FontWeight.Bold)
                }
            }
            
            // Cancel button
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        }
    }
    
    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onDismiss()
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Donation Posted!", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text("Your donation has been posted successfully! Volunteers will be notified and can claim it soon.")
            },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    onDismiss()
                }) {
                    Text("OK")
                }
            },
            icon = {
                Icon(
                    Icons.Default.Restaurant,
                    null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            }
        )
    }
}

