package com.nust.valentinegarage.feature.checkin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nust.valentinegarage.core.ui.components.ValentinePrimaryButton
import com.nust.valentinegarage.core.ui.components.ValentineTextField
import com.nust.valentinegarage.core.ui.theme.IndustrialOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewIntakeScreen(
    viewModel: NewIntakeViewModel,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var taskNameInput by remember { mutableStateOf("") }
    var taskDescInput by remember { mutableStateOf("") }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSuccess()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "NEW VEHICLE INTAKE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = IndustrialOrange,
                            letterSpacing = 1.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Header
            Text(
                "SERVICE PROTOCOL: VEHICLE REGISTRATION",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
            )
            Text(
                "Register an incoming vehicle for maintenance processing.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Section 2: Vehicle Details
            SectionLabel("VEHICLE DETAILS")

            ValentineTextField(
                value = uiState.licensePlate,
                onValueChange = viewModel::onLicensePlateChange,
                label = "LICENSE PLATE *",
                leadingIcon = Icons.Default.Create
            )

            ValentineTextField(
                value = uiState.vehicleModel,
                onValueChange = viewModel::onVehicleModelChange,
                label = "VEHICLE MODEL / MAKE *",
                leadingIcon = Icons.Default.Info
            )

            // Section 3: Odometer
            ValentineTextField(
                value = uiState.kilometersDriven,
                onValueChange = viewModel::onKilometersDrivenChange,
                label = "ODOMETER (KILOMETERS) *",
                leadingIcon = Icons.Default.Settings
            )

            // Section 4: Initial Condition Report
            SectionLabel("INTAKE CONDITION REPORT *")
            OutlinedTextField(
                value = uiState.initialCondition,
                onValueChange = viewModel::onInitialConditionChange,
                modifier = Modifier.fillMaxWidth().height(120.dp),
                placeholder = { Text("Describe the initial condition of the vehicle...") },
                shape = RoundedCornerShape(8.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Section 5: Maintenance Scope (Tasks)
            SectionLabel("MAINTENANCE SCOPE")

            ValentineTextField(
                value = taskNameInput,
                onValueChange = { taskNameInput = it },
                label = "TASK NAME",
                leadingIcon = Icons.Default.Build
            )

            ValentineTextField(
                value = taskDescInput,
                onValueChange = { taskDescInput = it },
                label = "TASK DESCRIPTION",
                leadingIcon = Icons.Default.Info
            )

            // ADD button
            Button(
                onClick = {
                    if (taskNameInput.isNotBlank()) {
                        viewModel.addTask(taskNameInput.trim(), taskDescInput.trim())
                        taskNameInput = ""
                        taskDescInput = ""
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndustrialOrange)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("ADD", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Black)
            }

            // Task Chips - displayed as a simple Column of chips
            uiState.tasks.forEach { task ->
                TaskChip(
                    text = task.name,
                    onRemove = { viewModel.removeTask(task) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error
            uiState.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Submit
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = IndustrialOrange,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                ValentinePrimaryButton(
                    text = "AUTHORIZE CHECK-IN",
                    onClick = viewModel::onAuthorizeCheckIn
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SectionLabel(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.sp
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun TaskChip(text: String, onRemove: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                Icons.Default.Close,
                contentDescription = "Remove",
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onRemove() },
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
