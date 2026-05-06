package com.nust.valentinegarage.feature.admin

import android.text.format.DateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nust.valentinegarage.core.model.CheckIn
import com.nust.valentinegarage.core.ui.theme.IndustrialOrange
import com.nust.valentinegarage.core.ui.theme.SteelBlue
import com.nust.valentinegarage.core.ui.theme.SuccessGreen
import com.nust.valentinegarage.core.ui.theme.WarningAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: AdminViewModel,
    onViewAudit: (String) -> Unit,
    onProfileTab: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "VALENTINE'S OVERVIEW",
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
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            Icons.Default.Dashboard,
                            contentDescription = "Overview",
                            tint = if (selectedTab == 0) IndustrialOrange else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            "OVERVIEW",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 0) IndustrialOrange else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        onProfileTab()
                    },
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = if (selectedTab == 1) IndustrialOrange else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            "PROFILE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTab == 1) IndustrialOrange else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Analytics Header: Horizontal scrolling stat cards
            item {
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    item {
                        AnalyticsStatCard(
                            label = "VEHICLES TODAY",
                            value = uiState.vehiclesToday.toString(),
                            icon = Icons.Default.LocalShipping
                        )
                    }
                    item {
                        AnalyticsStatCard(
                            label = "ACTIVE REPAIRS",
                            value = uiState.activeRepairs.toString(),
                            icon = Icons.Default.Build
                        )
                    }
                    item {
                        AnalyticsStatCard(
                            label = "COMPLETED TODAY",
                            value = uiState.completedToday.toString(),
                            icon = Icons.Default.CheckCircle
                        )
                    }
                }
            }

            // Search Bar
            item {
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    placeholder = { Text("Search License Plate") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
            }

            // Section header: Daily Log
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "DAILY LOG",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Check-in log list
            if (uiState.isLoading) {
                item {
                    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = IndustrialOrange)
                    }
                }
            } else if (uiState.filteredCheckIns.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text("No records found.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                items(uiState.filteredCheckIns) { checkIn ->
                    DailyLogItem(
                        checkIn = checkIn,
                        onClick = { onViewAudit(checkIn.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun AnalyticsStatCard(
    label: String,
    value: String,
    icon: ImageVector
) {
    Surface(
        modifier = Modifier.width(140.dp).height(120.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(icon, contentDescription = null, tint = IndustrialOrange, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.weight(1f))
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = IndustrialOrange
                )
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyLogItem(checkIn: CheckIn, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Time / Date
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    DateFormat.format("HH:mm", checkIn.timestamp).toString(),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    DateFormat.format("dd/MM", checkIn.timestamp).toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            // License plate & Model
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (checkIn.vehicleModel.isNotBlank()) "${checkIn.vehicleId} - ${checkIn.vehicleModel}" else checkIn.vehicleId,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    "By: ${checkIn.checkedInBy}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Status pill
            Surface(
                color = if (checkIn.isCompleted) SuccessGreen.copy(alpha = 0.15f) else IndustrialOrange.copy(alpha = 0.15f),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text(
                    if (checkIn.isCompleted) "COMPLETED" else "IN PROGRESS",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (checkIn.isCompleted) SuccessGreen else IndustrialOrange
                )
            }
        }
    }
}
