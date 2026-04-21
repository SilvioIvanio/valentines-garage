package com.nust.valentinegarage.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import com.nust.valentinegarage.core.model.AppThemeMode
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nust.valentinegarage.core.ui.theme.ErrorRed
import com.nust.valentinegarage.core.ui.theme.IndustrialOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    isMechanic: Boolean = true
) {
    val uiState by viewModel.uiState.collectAsState()
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onLogout()
        }
    }

    LaunchedEffect(uiState.showPasswordDialog) {
        if (!uiState.showPasswordDialog) {
            newPassword = ""
            confirmPassword = ""
        }
    }

    // Change Password Dialog
    if (uiState.showPasswordDialog) {
        AlertDialog(
            onDismissRequest = viewModel::dismissPasswordDialog,
            title = {
                Text("CHANGE PASSWORD", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black))
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("NEW PASSWORD") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("CONFIRM PASSWORD") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    uiState.passwordChangeMessage?.let { msg ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(msg, style = MaterialTheme.typography.bodySmall, color = ErrorRed)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.changePassword(newPassword, confirmPassword)
                        if (uiState.passwordChangeMessage == null) {
                            newPassword = ""
                            confirmPassword = ""
                        }
                    },
                    enabled = !uiState.isChangingPassword,
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndustrialOrange)
                ) {
                    if (uiState.isChangingPassword) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("UPDATE", color = Color.White, fontWeight = FontWeight.Black)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissPasswordDialog) { Text("CANCEL") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "MY PROFILE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = IndustrialOrange,
                            letterSpacing = 1.sp
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = onBack,
                    icon = {
                        Icon(
                            if (isMechanic) Icons.Default.Build else Icons.Default.Dashboard,
                            contentDescription = if (isMechanic) "Garage" else "Overview",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            if (isMechanic) "GARAGE" else "OVERVIEW",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = IndustrialOrange)
                    },
                    label = {
                        Text(
                            "PROFILE",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = IndustrialOrange)
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Orange rounded square avatar with initials
            Surface(
                modifier = Modifier.size(100.dp),
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(3.dp, IndustrialOrange)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = uiState.user?.initials ?: "?",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = IndustrialOrange,
                            fontSize = 36.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = uiState.user?.name ?: "LOADING...",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black)
            )

            Text(
                text = "ROLE: ${uiState.user?.role?.name ?: ""}",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Change Password row
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.showPasswordDialog() },
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        "Change Password",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    )
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Theme selection row
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.NightlightRound, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            "Theme Preference",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val options = listOf(AppThemeMode.LIGHT, AppThemeMode.DARK, AppThemeMode.SYSTEM)
                        options.forEachIndexed { index, mode ->
                            SegmentedButton(
                                selected = uiState.themeMode == mode,
                                onClick = { viewModel.setThemeMode(mode) },
                                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                                colors = SegmentedButtonDefaults.colors(
                                    activeContainerColor = IndustrialOrange,
                                    activeContentColor = Color.White,
                                    inactiveContainerColor = MaterialTheme.colorScheme.surface,
                                    inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                Text(
                                    mode.name, 
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (uiState.themeMode == mode) FontWeight.Black else FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Password change feedback
            uiState.passwordChangeMessage?.let { msg ->
                if (!uiState.showPasswordDialog) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(msg, style = MaterialTheme.typography.bodySmall, color = if (msg.startsWith("Failed")) ErrorRed else Color(0xFF2E7D32))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Deep Red SECURE LOGOUT button
            Button(
                onClick = viewModel::logout,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ErrorRed, contentColor = Color.White)
            ) {
                Text("SECURE LOGOUT", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black))
            }
        }
    }
}
