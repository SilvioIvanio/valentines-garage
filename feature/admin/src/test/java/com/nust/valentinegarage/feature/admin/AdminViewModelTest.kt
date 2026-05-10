package com.nust.valentinegarage.feature.admin

import com.google.common.truth.Truth.assertThat
import com.nust.valentinegarage.core.domain.repository.CheckInRepository
import com.nust.valentinegarage.core.model.CheckIn
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
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class AdminViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var checkInRepository: CheckInRepository
    private lateinit var viewModel: AdminViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        checkInRepository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when checkins emitted, analytics are computed correctly`() = runTest {
        // Arrange
        val startOfDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val yesterday = startOfDay - 86400000L

        val fakeCheckIns = listOf(
            CheckIn(id = "1", vehicleId = "N 111 W", vehicleModel = "Toyota Hilux", timestamp = startOfDay + 1000, kilometersDriven = 10, initialCondition = "Bad", checkedInBy = "Silvio", isCompleted = true),
            CheckIn(id = "2", vehicleId = "N 222 W", vehicleModel = "VW Golf", timestamp = startOfDay + 2000, kilometersDriven = 20, initialCondition = "Worse", checkedInBy = "Bob", isCompleted = false),
            CheckIn(id = "3", vehicleId = "N 333 W", vehicleModel = "Ford Ranger", timestamp = yesterday, kilometersDriven = 30, initialCondition = "Okay", checkedInBy = "Alice", isCompleted = false)
        )

        val flow = MutableStateFlow(fakeCheckIns)
        every { checkInRepository.getCheckInsFlow() } returns flow

        // Act
        viewModel = AdminViewModel(checkInRepository)
        testDispatcher.scheduler.advanceUntilIdle() // Process all coroutines

        val uiState = viewModel.uiState.first { !it.isLoading }

        // Assert
        // Vehicles today = 2 (id 1, 2)
        assertThat(uiState.vehiclesToday).isEqualTo(2)
        // Active repairs (not completed) = 2 (id 2, 3)
        assertThat(uiState.activeRepairs).isEqualTo(2)
        // Completed today = 1 (id 1)
        assertThat(uiState.completedToday).isEqualTo(1)
        // Total list should be 3
        assertThat(uiState.filteredCheckIns).hasSize(3)
    }

    @Test
    fun `when search query changes, filtered list updates correctly`() = runTest {
        // Arrange
        val fakeCheckIns = listOf(
            CheckIn(id = "1", vehicleId = "N 111 W", vehicleModel = "Toyota", timestamp = 0, kilometersDriven = 0, initialCondition = "", checkedInBy = "", isCompleted = false),
            CheckIn(id = "2", vehicleId = "N 222 W", vehicleModel = "VW", timestamp = 0, kilometersDriven = 0, initialCondition = "", checkedInBy = "", isCompleted = false)
        )

        val flow = MutableStateFlow(fakeCheckIns)
        every { checkInRepository.getCheckInsFlow() } returns flow
        viewModel = AdminViewModel(checkInRepository)

        // Act
        viewModel.onSearchQueryChange("111")
        testDispatcher.scheduler.advanceUntilIdle()

        val uiState = viewModel.uiState.first { !it.isLoading }

        // Assert
        assertThat(uiState.filteredCheckIns).hasSize(1)
        assertThat(uiState.filteredCheckIns[0].vehicleId).isEqualTo("N 111 W")
    }
}
