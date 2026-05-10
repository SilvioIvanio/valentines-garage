package com.nust.valentinegarage.core.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.nust.valentinegarage.core.data.dto.CheckInDto
import com.nust.valentinegarage.core.data.mapper.toDomain
import com.nust.valentinegarage.core.data.mapper.toDto
import com.nust.valentinegarage.core.domain.repository.CheckInRepository
import com.nust.valentinegarage.core.model.CheckIn
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CheckInRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CheckInRepository {

    override suspend fun createCheckIn(checkIn: CheckIn): Result<Unit> = try {
        val dto = checkIn.toDto()
        // If ID is empty, Firestore will generate one
        val docRef = if (dto.id.isEmpty()) {
            firestore.collection("check_ins").document()
        } else {
            firestore.collection("check_ins").document(dto.id)
        }
        
        docRef.set(dto.copy(id = docRef.id)).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getCheckInsFlow(): Flow<List<CheckIn>> = callbackFlow {
        val listener = firestore.collection("check_ins")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val checkIns = snapshot?.toObjects(CheckInDto::class.java)
                    ?.map { it.toDomain() } ?: emptyList()
                trySend(checkIns)
            }
        awaitClose { listener.remove() }
    }

    override fun getCheckInById(id: String): Flow<CheckIn?> = callbackFlow {
        val listener = firestore.collection("check_ins").document(id)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val checkIn = snapshot?.toObject(CheckInDto::class.java)?.toDomain()
                trySend(checkIn)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun completeCheckIn(checkInId: String): Result<Unit> = try {
        firestore.collection("check_ins").document(checkInId)
            .update(
                mapOf(
                    "isCompleted" to true,
                    "completed" to true
                )
            )
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
