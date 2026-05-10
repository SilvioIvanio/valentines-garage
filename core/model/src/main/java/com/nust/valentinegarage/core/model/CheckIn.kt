package com.nust.valentinegarage.core.model

data class CheckIn(
    val id: String,
    val vehicleId: String,
    val vehicleModel: String,       // Make / Model
    val timestamp: Long,            // Arrival time
    val kilometersDriven: Int,      // "Odometer" reading
    val initialCondition: String,
    val checkedInBy: String,        // Name (legacy)
    val checkedInById: String?,     // UID of the mechanic who logged it
    val isCompleted: Boolean        // Set to true when Admin clicks "VEHICLE CLEARED"
)
