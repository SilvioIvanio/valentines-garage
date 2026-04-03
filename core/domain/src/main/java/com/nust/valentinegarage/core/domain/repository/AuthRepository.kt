package com.nust.valentinegarage.core.domain.repository

import com.nust.valentinegarage.core.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, pass: String): Result<User>
    suspend fun logout()
    fun getCurrentUser(): Flow<User?>
    fun getUsers(): Flow<List<User>>
    suspend fun sendPasswordReset(email: String): Result<Unit>
    suspend fun updatePassword(newPassword: String): Result<Unit>
}
