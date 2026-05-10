package com.nust.valentinegarage.feature.mechanic

import com.google.common.truth.Truth.assertThat
import com.nust.valentinegarage.core.domain.repository.AuthRepository
import com.nust.valentinegarage.core.domain.repository.CheckInRepository
import com.nust.valentinegarage.core.domain.repository.TaskRepository
import com.nust.valentinegarage.core.model.CheckIn
import com.nust.valentinegarage.core.model.Task
import com.nust.valentinegarage.core.model.TaskPriority
import com.nust.valentinegarage.core.model.TaskStatus
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ServiceDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var checkInRepository: CheckInRepository
    private lateinit var taskRepository: TaskRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: ServiceDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        checkInRepository = mockk()
        taskRepository = mockk()
        authRepository = mockk()
        
        every { authRepository.getCurrentUser() } returns MutableStateFlow(null)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `tasks groupBy logic correctly routes items to exactly 3 distinct lists`() = runTest {
        // Arrange
        val checkInId = "checkin_123"
        val mockCheckIn = CheckIn(id = checkInId, vehicleId = "N 111 W", vehicleModel = "Toyota Hilux", timestamp = 0, kilometersDriven = 0, initialCondition = "", checkedInBy = "", isCompleted = false)
        
        // Create 5 fake tasks mixed with different statuses
        val fakeTasks = listOf(
            createTask("1", TaskStatus.TODO),
            createTask("2", TaskStatus.IN_PROGRESS),
            createTask("3", TaskStatus.DONE),
            createTask("4", TaskStatus.TODO),
            createTask("5", TaskStatus.DONE)
        )

        every { checkInRepository.getCheckInById(checkInId) } returns MutableStateFlow(mockCheckIn)
        every { taskRepository.getTasksForCheckIn(checkInId) } returns MutableStateFlow(fakeTasks)

        viewModel = ServiceDetailViewModel(checkInRepository, taskRepository, authRepository)

        // Act
        viewModel.loadServiceDetails(checkInId)
        testDispatcher.scheduler.advanceUntilIdle() // Ensure coroutines complete

        val uiState = viewModel.uiState.first { !it.isLoading }

        // Assert
        // We expect EXACTLY 2 TODO, 1 IN_PROGRESS, and 2 DONE tasks.
        assertThat(uiState.todoTasks).hasSize(2)
        assertThat(uiState.todoTasks.map { it.id }).containsExactly("1", "4")

        assertThat(uiState.inProgressTasks).hasSize(1)
        assertThat(uiState.inProgressTasks[0].id).isEqualTo("2")

        assertThat(uiState.doneTasks).hasSize(2)
        assertThat(uiState.doneTasks.map { it.id }).containsExactly("3", "5")
        
        // Ensure no task was dropped or duplicated across the 3 lists
        val totalRoutedTasks = uiState.todoTasks.size + uiState.inProgressTasks.size + uiState.doneTasks.size
        assertThat(totalRoutedTasks).isEqualTo(5)
    }

    private fun createTask(id: String, status: TaskStatus): Task {
        return Task(
            id = id,
            name = "Test Task $id",
            description = "Description $id",
            status = status,
            priority = TaskPriority.NORMAL,
            mechanicId = null,
            mechanicName = null,
            mechanicInitials = null,
            completedAt = null,
            notes = ""
        )
    }
}
