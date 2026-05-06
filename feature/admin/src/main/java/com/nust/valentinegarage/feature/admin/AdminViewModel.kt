package com.nust.valentinegarage.feature.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nust.valentinegarage.core.domain.repository.AuthRepository
import com.nust.valentinegarage.core.domain.repository.CheckInRepository
import com.nust.valentinegarage.core.model.CheckIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class AdminDashboardUiState(
    val vehiclesToday: Int = 0,
    val activeRepairs: Int = 0,
    val completedToday: Int = 0,
    val allCheckIns: List<CheckIn> = emptyList(),
    val filteredCheckIns: List<CheckIn> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val checkInRepository: CheckInRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<AdminDashboardUiState> = combine(
        checkInRepository.getCheckInsFlow(),
        authRepository.getUsers(),
        _searchQuery
    ) { checkIns, users, query ->
        val resolvedCheckIns = checkIns.map { checkIn ->
            val user = users.find { it.id == checkIn.checkedInById }
            if (user != null) {
                checkIn.copy(checkedInBy = user.name)
            } else {
                checkIn
            }
        }

        val startOfDay = getStartOfDay()
        val todayCheckIns = resolvedCheckIns.filter { it.timestamp >= startOfDay }

        val filtered = if (query.isBlank()) {
            resolvedCheckIns
        } else {
            resolvedCheckIns.filter { it.vehicleId.contains(query, ignoreCase = true) }
        }

        AdminDashboardUiState(
            vehiclesToday = todayCheckIns.size,
            activeRepairs = resolvedCheckIns.count { !it.isCompleted },
            completedToday = todayCheckIns.count { it.isCompleted },
            allCheckIns = resolvedCheckIns,
            filteredCheckIns = filtered.sortedByDescending { it.timestamp },
            searchQuery = query,
            isLoading = false
        )
    }
        .onStart { emit(AdminDashboardUiState(isLoading = true)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AdminDashboardUiState(isLoading = true)
        )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private fun getStartOfDay(): Long {
        val cal = java.util.Calendar.getInstance()
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
