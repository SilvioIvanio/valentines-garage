package com.nust.valentinegarage.core.data.dto

data class CheckInDto(
    val id: String = "",
    val vehicleId: String = "",
    val vehicleModel: String = "",
    val timestamp: Long = 0L,
    val kilometersDriven: Int = 0,
    val initialCondition: String = "",
    val checkedInBy: String = "",
    val checkedInById: String? = null,
    @get:com.google.firebase.firestore.PropertyName("isCompleted")
    @set:com.google.firebase.firestore.PropertyName("isCompleted")
    var isCompleted: Boolean = false
)
