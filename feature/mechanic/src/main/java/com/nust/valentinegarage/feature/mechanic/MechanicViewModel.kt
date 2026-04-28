package com.nust.valentinegarage.feature.mechanic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nust.valentinegarage.core.domain.repository.CheckInRepository
import com.nust.valentinegarage.core.domain.repository.TaskRepository
import com.nust.valentinegarage.core.model.CheckIn
import com.nust.valentinegarage.core.model.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class ActiveRepairItem(
    val checkIn: CheckIn,
    val progress: Float,       // 0f to 1f
    val totalTasks: Int,
    val completedTasks: Int
)

data class MechanicDashboardUiState(
    val activeRepairs: List<ActiveRepairItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MechanicViewModel @Inject constructor(
    private val checkInRepository: CheckInRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    val uiState: StateFlow<MechanicDashboardUiState> = checkInRepository.getCheckInsFlow()
        .flatMapLatest { checkIns ->
            val activeCheckIns = checkIns.filter { !it.isCompleted }

            if (activeCheckIns.isEmpty()) {
                flowOf(MechanicDashboardUiState(activeRepairs = emptyList(), isLoading = false))
            } else {
                // Combine task flows for all active check-ins to compute real progress
                val taskFlows = activeCheckIns.map { checkIn ->
                    taskRepository.getTasksForCheckIn(checkIn.id).map { tasks ->
                        val total = tasks.size
                        val done = tasks.count { it.status == TaskStatus.DONE }
                        ActiveRepairItem(
                            checkIn = checkIn,
                            progress = if (total > 0) done.toFloat() / total.toFloat() else 0f,
                            totalTasks = total,
                            completedTasks = done
                        )
                    }
                }
                combine(taskFlows) { repairItems ->
                    MechanicDashboardUiState(
                        activeRepairs = repairItems.toList(),
                        isLoading = false
                    )
                }
            }
        }
        .onStart { emit(MechanicDashboardUiState(isLoading = true)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MechanicDashboardUiState(isLoading = true)
        )
}
