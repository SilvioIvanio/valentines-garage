package com.nust.valentinegarage.core.model

data class Task(
    val id: String,
    val name: String,               // e.g., "Oil Filter Replacement"
    val description: String,        // e.g., "Full synthetic 5W-30 replacement..."
    val status: TaskStatus,
    val priority: TaskPriority,
    val mechanicId: String?,        // Accountability: WHO claimed/finished it
    val mechanicName: String?,
    val mechanicInitials: String?,  // e.g., "JD"
    val completedAt: Long?,         // Feeds the "Actioned by... at 14:35" Audit UI
    val notes: String               // Diagnostic notes added upon completion
)

enum class TaskStatus { TODO, IN_PROGRESS, DONE }
enum class TaskPriority { HIGH, NORMAL, LOW }
