package com.nust.valentinegarage.core.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.nust.valentinegarage.core.model.AppThemeMode
import com.nust.valentinegarage.core.domain.repository.ThemeRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ThemeRepositoryImpl(context: Context) : ThemeRepository {
    private val prefs: SharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    override fun getThemeMode(userId: String): Flow<AppThemeMode> = callbackFlow {
        val key = "theme_mode_$userId"
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { p, k ->
            if (k == key) {
                val modeStr = p.getString(key, AppThemeMode.SYSTEM.name) ?: AppThemeMode.SYSTEM.name
                trySend(AppThemeMode.valueOf(modeStr))
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        
        val modeStr = prefs.getString(key, AppThemeMode.SYSTEM.name) ?: AppThemeMode.SYSTEM.name
        trySend(AppThemeMode.valueOf(modeStr))
        
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override suspend fun setThemeMode(userId: String, mode: AppThemeMode) {
        prefs.edit().putString("theme_mode_$userId", mode.name).apply()
    }
}
