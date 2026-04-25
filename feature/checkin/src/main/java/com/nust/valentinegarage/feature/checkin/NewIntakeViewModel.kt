package com.nust.valentinegarage.feature.checkin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nust.valentinegarage.core.domain.repository.AuthRepository
import com.nust.valentinegarage.core.domain.repository.CheckInRepository
import com.nust.valentinegarage.core.domain.repository.TaskRepository
import com.nust.valentinegarage.core.model.CheckIn
import com.nust.valentinegarage.core.model.Task
import com.nust.valentinegarage.core.model.TaskPriority
import com.nust.valentinegarage.core.model.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

data class TaskEntry(
    val name: String,
    val description: String
)

data class NewIntakeUiState(
    val licensePlate: String = "",
    val vehicleModel: String = "",
    val kilometersDriven: String = "",
    val initialCondition: String = "",
    val tasks: List<TaskEntry> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class NewIntakeViewModel @Inject constructor(
    private val checkInRepository: CheckInRepository,
    private val taskRepository: TaskRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewIntakeUiState())
    val uiState: StateFlow<NewIntakeUiState> = _uiState.asStateFlow()

    fun onLicensePlateChange(value: String) {
        _uiState.update { it.copy(licensePlate = value) }
    }

    fun onVehicleModelChange(value: String) {
        _uiState.update { it.copy(vehicleModel = value) }
    }

    fun onKilometersDrivenChange(value: String) {
        _uiState.update { it.copy(kilometersDriven = value) }
    }

    fun onInitialConditionChange(value: String) {
        _uiState.update { it.copy(initialCondition = value) }
    }

    fun addTask(name: String, description: String) {
        if (name.isBlank()) return
        _uiState.update { it.copy(tasks = it.tasks + TaskEntry(name, description)) }
    }

    fun removeTask(task: TaskEntry) {
        _uiState.update { it.copy(tasks = it.tasks - task) }
    }

    fun onAuthorizeCheckIn() {
        val state = _uiState.value
        
        // Comprehensive Validation
        val errorMsg = when {
            state.licensePlate.isBlank() -> "License Plate is required."
            state.vehicleModel.isBlank() -> "Vehicle Model / Make is required."
            state.kilometersDriven.isBlank() -> "Odometer reading is required."
            state.initialCondition.isBlank() -> "Initial Condition report is required."
            else -> null
        }

        if (errorMsg != null) {
            _uiState.update { it.copy(error = errorMsg) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val user = authRepository.getCurrentUser().firstOrNull()
            val mechanicName = user?.name ?: "Unknown"

            val checkInId = UUID.randomUUID().toString()
            val newCheckIn = CheckIn(
                id = checkInId,
                vehicleId = state.licensePlate,
                vehicleModel = state.vehicleModel,
                timestamp = System.currentTimeMillis(),
                kilometersDriven = state.kilometersDriven.toIntOrNull() ?: 0,
                initialCondition = state.initialCondition,
                checkedInBy = mechanicName,
                checkedInById = user?.id,
                isCompleted = false
            )

            checkInRepository.createCheckIn(newCheckIn)
                .onSuccess {
                    // Now create the tasks as sub-collection items
                    state.tasks.forEach { taskEntry ->
                        val task = Task(
                            id = UUID.randomUUID().toString(),
                            name = taskEntry.name,
                            description = taskEntry.description,
                            status = TaskStatus.TODO,
                            priority = TaskPriority.NORMAL,
                            mechanicId = null,
                            mechanicName = null,
                            mechanicInitials = null,
                            completedAt = null,
                            notes = ""
                        )
                        taskRepository.createTask(checkInId, task)
                    }
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    fun resetState() {
        _uiState.value = NewIntakeUiState()
    }
}
