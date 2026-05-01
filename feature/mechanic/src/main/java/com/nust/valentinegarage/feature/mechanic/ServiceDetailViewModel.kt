package com.nust.valentinegarage.feature.mechanic

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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ServiceBoardUiState(
    val checkIn: CheckIn? = null,
    val todoTasks: List<Task> = emptyList(),
    val inProgressTasks: List<Task> = emptyList(),
    val doneTasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showCompleteDialog: Boolean = false,
    val taskToComplete: Task? = null,
    val showAddTaskDialog: Boolean = false
)

@HiltViewModel
class ServiceDetailViewModel @Inject constructor(
    private val checkInRepository: CheckInRepository,
    private val taskRepository: TaskRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServiceBoardUiState())
    val uiState: StateFlow<ServiceBoardUiState> = _uiState.asStateFlow()

    // Expose current mechanic ID for accountability checks
    val currentMechanicId: StateFlow<String?> = authRepository.getCurrentUser()
        .map { it?.id }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun loadServiceDetails(checkInId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            combine(
                checkInRepository.getCheckInById(checkInId),
                taskRepository.getTasksForCheckIn(checkInId),
                authRepository.getUsers()
            ) { checkIn, tasks, users ->
                val resolvedTasks = tasks.map { task ->
                    val user = users.find { it.id == task.mechanicId }
                    if (user != null) {
                        task.copy(mechanicName = user.name, mechanicInitials = user.initials)
                    } else {
                        task
                    }
                }
                val grouped = resolvedTasks.groupBy { it.status }
                ServiceBoardUiState(
                    checkIn = checkIn,
                    todoTasks = grouped[TaskStatus.TODO] ?: emptyList(),
                    inProgressTasks = grouped[TaskStatus.IN_PROGRESS] ?: emptyList(),
                    doneTasks = grouped[TaskStatus.DONE] ?: emptyList(),
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun onStartWork(task: Task) {
        val checkInId = _uiState.value.checkIn?.id ?: return
        viewModelScope.launch {
            val user = authRepository.getCurrentUser().firstOrNull() ?: return@launch
            taskRepository.claimTask(
                checkInId = checkInId,
                taskId = task.id,
                mechanicId = user.id,
                mechanicName = user.name,
                mechanicInitials = user.initials
            )
        }
    }

    fun onRequestComplete(task: Task) {
        _uiState.update { it.copy(showCompleteDialog = true, taskToComplete = task) }
    }

    fun onDismissCompleteDialog() {
        _uiState.update { it.copy(showCompleteDialog = false, taskToComplete = null) }
    }

    fun onConfirmComplete(notes: String) {
        val checkInId = _uiState.value.checkIn?.id ?: return
        val taskId = _uiState.value.taskToComplete?.id ?: return
        viewModelScope.launch {
            taskRepository.completeTask(
                checkInId = checkInId,
                taskId = taskId,
                notes = notes
            )
            _uiState.update { it.copy(showCompleteDialog = false, taskToComplete = null) }
        }
    }

    // ── Add Task from Service Board ──
    fun showAddTaskDialog() {
        _uiState.update { it.copy(showAddTaskDialog = true) }
    }

    fun dismissAddTaskDialog() {
        _uiState.update { it.copy(showAddTaskDialog = false) }
    }

    fun addTask(name: String, description: String) {
        val checkInId = _uiState.value.checkIn?.id ?: return
        if (name.isBlank()) return
        viewModelScope.launch {
            val task = Task(
                id = UUID.randomUUID().toString(),
                name = name,
                description = description,
                status = TaskStatus.TODO,
                priority = TaskPriority.NORMAL,
                mechanicId = null,
                mechanicName = null,
                mechanicInitials = null,
                completedAt = null,
                notes = ""
            )
            taskRepository.createTask(checkInId, task)
            _uiState.update { it.copy(showAddTaskDialog = false) }
        }
    }

    // ── Complete Entire Repair ──
    fun completeRepair() {
        val checkInId = _uiState.value.checkIn?.id ?: return
        viewModelScope.launch {
            checkInRepository.completeCheckIn(checkInId)
        }
    }
}
