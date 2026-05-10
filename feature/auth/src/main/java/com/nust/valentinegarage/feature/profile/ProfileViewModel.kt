package com.nust.valentinegarage.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nust.valentinegarage.core.domain.repository.AuthRepository
import com.nust.valentinegarage.core.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.nust.valentinegarage.core.domain.repository.ThemeRepository
import com.nust.valentinegarage.core.model.AppThemeMode
import javax.inject.Inject

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false,
    val showPasswordDialog: Boolean = false,
    val passwordChangeMessage: String? = null,
    val isChangingPassword: Boolean = false,
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val themeRepository: ThemeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            authRepository.getCurrentUser().collect { user ->
                _uiState.update { it.copy(user = user, isLoading = false) }
            }
        }
        viewModelScope.launch {
            authRepository.getCurrentUser().filterNotNull().flatMapLatest { user ->
                themeRepository.getThemeMode(user.id)
            }.collect { mode ->
                _uiState.update { it.copy(themeMode = mode) }
            }
        }
    }

    fun setThemeMode(mode: AppThemeMode) {
        val userId = _uiState.value.user?.id ?: return
        viewModelScope.launch {
            themeRepository.setThemeMode(userId, mode)
        }
    }

    fun showPasswordDialog() {
        _uiState.update { it.copy(showPasswordDialog = true, passwordChangeMessage = null) }
    }

    fun dismissPasswordDialog() {
        _uiState.update { it.copy(showPasswordDialog = false, passwordChangeMessage = null) }
    }

    fun changePassword(newPassword: String, confirmPassword: String) {
        if (newPassword.length < 6) {
            _uiState.update { it.copy(passwordChangeMessage = "Password must be at least 6 characters.") }
            return
        }
        if (newPassword != confirmPassword) {
            _uiState.update { it.copy(passwordChangeMessage = "Passwords do not match.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isChangingPassword = true) }
            authRepository.updatePassword(newPassword)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isChangingPassword = false,
                            showPasswordDialog = false,
                            passwordChangeMessage = "Password updated successfully."
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isChangingPassword = false,
                            passwordChangeMessage = "Failed: ${e.message}"
                        )
                    }
                }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(passwordChangeMessage = null) }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.update { it.copy(isLoggedOut = true) }
        }
    }
}
