package com.nust.valentinegarage.core.data.dto

data class TaskDto(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val status: String = "TODO",
    val priority: String = "NORMAL",
    val mechanicId: String? = null,
    val mechanicName: String? = null,
    val mechanicInitials: String? = null,
    val completedAt: Long? = null,
    val notes: String = ""
)
