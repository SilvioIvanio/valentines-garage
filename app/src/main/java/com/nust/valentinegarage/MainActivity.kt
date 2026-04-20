package com.nust.valentinegarage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nust.valentinegarage.core.model.AppThemeMode
import kotlinx.coroutines.flow.flowOf
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nust.valentinegarage.core.domain.repository.AuthRepository
import com.nust.valentinegarage.core.ui.theme.ValentineGarageTheme
import com.nust.valentinegarage.core.domain.repository.ThemeRepository
import javax.inject.Inject
import com.nust.valentinegarage.feature.admin.AdminDashboardScreen
import com.nust.valentinegarage.feature.admin.AuditTrailScreen
import com.nust.valentinegarage.feature.auth.LoginScreen
import com.nust.valentinegarage.feature.checkin.NewIntakeScreen
import com.nust.valentinegarage.feature.mechanic.MechanicDashboardScreen
import com.nust.valentinegarage.feature.mechanic.ServiceDetailScreen
import com.nust.valentinegarage.feature.profile.ProfileScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var themeRepository: ThemeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val user by authRepository.getCurrentUser().collectAsState(initial = null)
            val themeMode by remember(user) {
                if (user != null) {
                    themeRepository.getThemeMode(user!!.id)
                } else {
                    flowOf(AppThemeMode.SYSTEM)
                }
            }.collectAsState(initial = AppThemeMode.SYSTEM)
            
            ValentineGarageTheme(themeMode = themeMode) {
                val navController = rememberNavController()
                
                // Track initial state to avoid flicker
                var startDestination by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(Unit) {
                    // Check current user from cache/auth
                    authRepository.getCurrentUser().collect { user ->
                        if (startDestination == null) {
                            startDestination = if (user != null) {
                                if (user.role.name == "ADMIN") "admin_dashboard" else "mechanic_dashboard"
                            } else {
                                "login"
                            }
                        }
                    }
                }

                if (startDestination != null) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination!!
                    ) {
                    // Shared Entry Point
                    composable("login") {
                        LoginScreen(
                            viewModel = hiltViewModel(),
                            onLoginSuccess = { role ->
                                val target = if (role == "ADMIN") "admin_dashboard" else "mechanic_dashboard"
                                navController.navigate(target) {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }

                    // ── Mechanic Flow ──
                    composable("mechanic_dashboard") {
                        MechanicDashboardScreen(
                            viewModel = hiltViewModel(),
                            onNewCheckIn = { navController.navigate("new_intake") },
                            onViewDetail = { id -> navController.navigate("service_board/$id") },
                            onProfileTab = { navController.navigate("mechanic_profile") }
                        )
                    }

                    composable("new_intake") {
                        NewIntakeScreen(
                            viewModel = hiltViewModel(),
                            onBack = { navController.popBackStack() },
                            onSuccess = { navController.popBackStack() }
                        )
                    }

                    composable("service_board/{checkInId}") { backStackEntry ->
                        val checkInId = backStackEntry.arguments?.getString("checkInId") ?: ""
                        ServiceDetailScreen(
                            checkInId = checkInId,
                            viewModel = hiltViewModel(),
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // ── Admin Flow ──
                    composable("admin_dashboard") {
                        AdminDashboardScreen(
                            viewModel = hiltViewModel(),
                            onViewAudit = { id -> navController.navigate("audit/$id") },
                            onProfileTab = { navController.navigate("admin_profile") }
                        )
                    }

                    composable("audit/{checkInId}") { backStackEntry ->
                        val checkInId = backStackEntry.arguments?.getString("checkInId") ?: ""
                        AuditTrailScreen(
                            checkInId = checkInId,
                            viewModel = hiltViewModel(),
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // ── Shared Profile ──
                    composable("mechanic_profile") {
                        ProfileScreen(
                            viewModel = hiltViewModel(),
                            onBack = { navController.popBackStack() },
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            isMechanic = true
                        )
                    }
                    composable("admin_profile") {
                        ProfileScreen(
                            viewModel = hiltViewModel(),
                            onBack = { navController.popBackStack() },
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            isMechanic = false
                        )
                    }
                    }
                } else {
                    // Simple loading screen while deciding route
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = com.nust.valentinegarage.core.ui.theme.IndustrialOrange)
                    }
                }
            }
        }
    }
}