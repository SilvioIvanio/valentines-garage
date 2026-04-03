package com.nust.valentinegarage.core.domain.repository

import com.nust.valentinegarage.core.model.CheckIn
import kotlinx.coroutines.flow.Flow

interface CheckInRepository {
    suspend fun createCheckIn(checkIn: CheckIn): Result<Unit>
    fun getCheckInsFlow(): Flow<List<CheckIn>>
    fun getCheckInById(id: String): Flow<CheckIn?>
    suspend fun completeCheckIn(checkInId: String): Result<Unit>
}
