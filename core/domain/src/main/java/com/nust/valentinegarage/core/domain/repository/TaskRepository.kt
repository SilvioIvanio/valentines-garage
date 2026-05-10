package com.nust.valentinegarage.core.domain.repository

import com.nust.valentinegarage.core.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasksForCheckIn(checkInId: String): Flow<List<Task>>
    suspend fun createTask(checkInId: String, task: Task): Result<Unit>
    suspend fun claimTask(
        checkInId: String, 
        taskId: String, 
        mechanicId: String, 
        mechanicName: String, 
        mechanicInitials: String
    ): Result<Unit>
    
    suspend fun completeTask(
        checkInId: String, 
        taskId: String, 
        notes: String
    ): Result<Unit>
}
