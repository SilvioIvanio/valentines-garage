package com.nust.valentinegarage.core.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.nust.valentinegarage.core.data.dto.TaskDto
import com.nust.valentinegarage.core.data.mapper.toDomain
import com.nust.valentinegarage.core.domain.repository.TaskRepository
import com.nust.valentinegarage.core.model.Task
import com.nust.valentinegarage.core.model.TaskStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : TaskRepository {

    override fun getTasksForCheckIn(checkInId: String): Flow<List<Task>> = callbackFlow {
        val listener = firestore.collection("check_ins").document(checkInId)
            .collection("tasks")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val tasks = snapshot?.toObjects(TaskDto::class.java)
                    ?.map { it.toDomain() } ?: emptyList()
                trySend(tasks)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun createTask(checkInId: String, task: Task): Result<Unit> = try {
        val taskData = hashMapOf(
            "id" to task.id,
            "name" to task.name,
            "description" to task.description,
            "status" to task.status.name,
            "priority" to task.priority.name,
            "mechanicId" to task.mechanicId,
            "mechanicName" to task.mechanicName,
            "mechanicInitials" to task.mechanicInitials,
            "completedAt" to task.completedAt,
            "notes" to task.notes
        )
        firestore.collection("check_ins").document(checkInId)
            .collection("tasks").document(task.id)
            .set(taskData).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun claimTask(
        checkInId: String,
        taskId: String,
        mechanicId: String,
        mechanicName: String,
        mechanicInitials: String
    ): Result<Unit> = try {
        val taskRef = firestore.collection("check_ins").document(checkInId)
            .collection("tasks").document(taskId)

        // For offline support, we avoid transactions which require network.
        // We read instantly from cache to avoid the 10-second offline timeout delay
        val snapshot = try {
            taskRef.get(com.google.firebase.firestore.Source.CACHE).await()
        } catch (e: Exception) {
            taskRef.get().await() // Fallback if cache misses
        }
        val currentStatus = snapshot.getString("status") ?: "TODO"

        if (currentStatus != "TODO") {
            throw Exception("Task already claimed by another technician")
        }

        taskRef.update(
            mapOf(
                "status" to TaskStatus.IN_PROGRESS.name,
                "mechanicId" to mechanicId,
                "mechanicName" to mechanicName,
                "mechanicInitials" to mechanicInitials
            )
        ).await()
        
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun completeTask(
        checkInId: String,
        taskId: String,
        notes: String
    ): Result<Unit> = try {
        val taskRef = firestore.collection("check_ins").document(checkInId)
            .collection("tasks").document(taskId)

        taskRef.update(
            mapOf(
                "status" to TaskStatus.DONE.name,
                "notes" to notes,
                "completedAt" to System.currentTimeMillis()
            )
        ).await()
        
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
