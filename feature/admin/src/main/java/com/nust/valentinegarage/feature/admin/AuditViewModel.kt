package com.nust.valentinegarage.feature.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nust.valentinegarage.core.domain.repository.AuthRepository
import com.nust.valentinegarage.core.domain.repository.CheckInRepository
import com.nust.valentinegarage.core.domain.repository.TaskRepository
import com.nust.valentinegarage.core.model.CheckIn
import com.nust.valentinegarage.core.model.Task
import com.nust.valentinegarage.core.model.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuditTrailUiState(
    val checkIn: CheckIn? = null,
    val tasks: List<Task> = emptyList(),
    val verifiedCount: Int = 0,
    val isLoading: Boolean = false,
    val vehicleCleared: Boolean = false
)

@HiltViewModel
class AuditViewModel @Inject constructor(
    private val checkInRepository: CheckInRepository,
    private val taskRepository: TaskRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuditTrailUiState())
    val uiState: StateFlow<AuditTrailUiState> = _uiState.asStateFlow()

    fun loadAudit(checkInId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            combine(
                checkInRepository.getCheckInById(checkInId),
                taskRepository.getTasksForCheckIn(checkInId),
                authRepository.getUsers()
            ) { checkIn, tasks, users ->
                val resolvedCheckIn = checkIn?.let { ci ->
                    val user = users.find { it.id == ci.checkedInById }
                    if (user != null) ci.copy(checkedInBy = user.name) else ci
                }

                val resolvedTasks = tasks.filter { it.status == TaskStatus.DONE }.map { task ->
                    val user = users.find { it.id == task.mechanicId }
                    if (user != null) {
                        task.copy(mechanicName = user.name, mechanicInitials = user.initials)
                    } else {
                        task
                    }
                }

                AuditTrailUiState(
                    checkIn = resolvedCheckIn,
                    tasks = resolvedTasks,
                    verifiedCount = resolvedTasks.size,
                    isLoading = false,
                    vehicleCleared = checkIn?.isCompleted == true
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }


}
