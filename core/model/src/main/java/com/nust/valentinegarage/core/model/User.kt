package com.nust.valentinegarage.core.model

data class User(
    val id: String,                // Firebase Auth UID
    val name: String,              // e.g., "John Doe"
    val initials: String,          // e.g., "JD" (used for UI avatars)
    val role: Role                 // Enum: MECHANIC or ADMIN
)

enum class Role { MECHANIC, ADMIN }
