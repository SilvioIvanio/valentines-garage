package com.nust.valentinegarage

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.nust.valentinegarage.core.domain.repository.AuthRepository
import com.nust.valentinegarage.core.domain.repository.CheckInRepository
import com.nust.valentinegarage.core.domain.repository.TaskRepository
import com.nust.valentinegarage.feature.checkin.NewIntakeScreen
import com.nust.valentinegarage.feature.checkin.NewIntakeViewModel
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class IntakeUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testIntakeFormInputAndButton() {
        // Arrange
        val checkInRepo = mockk<CheckInRepository>(relaxed = true)
        val taskRepo = mockk<TaskRepository>(relaxed = true)
        val authRepo = mockk<AuthRepository>(relaxed = true)
        val viewModel = NewIntakeViewModel(checkInRepo, taskRepo, authRepo)

        composeTestRule.setContent {
            NewIntakeScreen(
                viewModel = viewModel,
                onBack = {},
                onSuccess = {}
            )
        }

        // Act & Assert
        // 1. Test layout insertion: LICENSE PLATE
        composeTestRule.onNodeWithText("LICENSE PLATE")
            .performTextInput("N 123 W")

        // 2. Test clicking: AUTHORIZE CHECK-IN
        composeTestRule.onNodeWithText("AUTHORIZE CHECK-IN")
            .performClick()
    }
}
