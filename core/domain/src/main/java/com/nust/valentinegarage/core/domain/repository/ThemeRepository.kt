package com.nust.valentinegarage.core.domain.repository

import com.nust.valentinegarage.core.model.AppThemeMode
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getThemeMode(userId: String): Flow<AppThemeMode>
    suspend fun setThemeMode(userId: String, mode: AppThemeMode)
}
