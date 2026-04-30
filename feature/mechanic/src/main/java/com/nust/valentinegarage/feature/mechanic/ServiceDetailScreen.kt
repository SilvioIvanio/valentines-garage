package com.nust.valentinegarage.feature.mechanic

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nust.valentinegarage.core.model.Task
import com.nust.valentinegarage.core.model.TaskStatus
import com.nust.valentinegarage.core.ui.theme.IndustrialOrange
import com.nust.valentinegarage.core.ui.theme.SuccessGreen
import com.nust.valentinegarage.core.ui.theme.SteelBlue
import com.nust.valentinegarage.core.ui.theme.WarningAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailScreen(
    checkInId: String,
    viewModel: ServiceDetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var completionNotes by remember { mutableStateOf("") }
    var addTaskName by remember { mutableStateOf("") }
    var addTaskDesc by remember { mutableStateOf("") }
    val currentMechanicId by viewModel.currentMechanicId.collectAsState()

    LaunchedEffect(checkInId) {
        viewModel.loadServiceDetails(checkInId)
    }

    // Completion Dialog
    if (uiState.showCompleteDialog) {
        AlertDialog(
            onDismissRequest = viewModel::onDismissCompleteDialog,
            title = {
                Text(
                    "COMPLETE TASK",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                )
            },
            text = {
                Column {
                    Text(
                        uiState.taskToComplete?.name ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = completionNotes,
                        onValueChange = { completionNotes = it },
                        label = { Text("REPAIR NOTES / PARTS USED") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        maxLines = 5
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onConfirmComplete(completionNotes)
                        completionNotes = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("MARK AS DONE ✔", color = Color.White, fontWeight = FontWeight.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::onDismissCompleteDialog) {
                    Text("CANCEL")
                }
            }
        )
    }

    // Add Task Dialog
    if (uiState.showAddTaskDialog) {
        AlertDialog(
            onDismissRequest = viewModel::dismissAddTaskDialog,
            title = {
                Text(
                    "ADD NEW TASK",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = addTaskName,
                        onValueChange = { addTaskName = it },
                        label = { Text("TASK NAME") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(4.dp)
                    )
                    OutlinedTextField(
                        value = addTaskDesc,
                        onValueChange = { addTaskDesc = it },
                        label = { Text("TASK DESCRIPTION") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        maxLines = 4,
                        shape = RoundedCornerShape(4.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addTask(addTaskName.trim(), addTaskDesc.trim())
                        addTaskName = ""
                        addTaskDesc = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IndustrialOrange),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("ADD TASK", color = Color.White, fontWeight = FontWeight.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissAddTaskDialog) {
                    Text("CANCEL")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        uiState.checkIn?.vehicleId ?: "LOADING...",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = IndustrialOrange
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            // Show FAB on TODO tab for adding tasks
            if (selectedTab == 0) {
                FloatingActionButton(
                    onClick = viewModel::showAddTaskDialog,
                    containerColor = IndustrialOrange,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {

            // Header Card: Odometer + Condition
            uiState.checkIn?.let { checkIn ->
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (checkIn.vehicleModel.isNotBlank()) {
                            Text(
                                text = checkIn.vehicleModel.uppercase(),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = IndustrialOrange
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Settings, contentDescription = null, tint = SteelBlue, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("ODOMETER", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant))
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("${checkIn.kilometersDriven} KM", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Black))
                            }
                            Column(modifier = Modifier.weight(1.5f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = SteelBlue, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("CONDITION", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant))
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    checkIn.initialCondition.ifBlank { "N/A" },
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Black)
                                )
                            }
                        }
                    }
                }
            }

            // TabRow: TODO | IN PROGRESS | DONE
            val tabs = listOf("TO DO", "IN PROGRESS", "DONE")
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = IndustrialOrange
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedTab == index) IndustrialOrange else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    )
                }
            }

            // Task List based on selected tab
            val tasks = when (selectedTab) {
                0 -> uiState.todoTasks
                1 -> uiState.inProgressTasks
                2 -> uiState.doneTasks
                else -> emptyList()
            }

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = IndustrialOrange)
                }
            } else if (tasks.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No tasks in this category.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (selectedTab == 0) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Tap + to add a task.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(tasks) { task ->
                        when (selectedTab) {
                            0 -> TodoTaskCard(task = task, onStartWork = { viewModel.onStartWork(task) })
                            1 -> InProgressTaskCard(task = task, onFinish = { viewModel.onRequestComplete(task) }, isOwner = task.mechanicId == currentMechanicId)
                            2 -> DoneTaskCard(task = task)
                        }
                    }
                    
                    if (selectedTab == 2 && uiState.todoTasks.isEmpty() && uiState.inProgressTasks.isEmpty() && uiState.doneTasks.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { 
                                    viewModel.completeRepair()
                                    onBack() 
                                },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text("FINALIZE REPAIR", color = Color.White, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodoTaskCard(task: Task, onStartWork: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(task.name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            if (task.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(task.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onStartWork,
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = IndustrialOrange),
                    border = androidx.compose.foundation.BorderStroke(1.dp, IndustrialOrange)
                ) {
                    Text("START WORK", fontWeight = FontWeight.Black, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun InProgressTaskCard(task: Task, onFinish: () -> Unit, isOwner: Boolean) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(task.name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            if (task.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(task.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(32.dp).background(IndustrialOrange.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            task.mechanicInitials ?: "?",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, color = IndustrialOrange)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        task.mechanicName ?: "Assigned",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (isOwner) {
                    Button(
                        onClick = onFinish,
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Text("FINISH & ADD NOTES", color = Color.White, fontWeight = FontWeight.Black, style = MaterialTheme.typography.labelSmall)
                    }
                } else {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            "ASSIGNED TO ${task.mechanicName?.uppercase() ?: "OTHER"}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DoneTaskCard(task: Task) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.06f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    task.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.LineThrough,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Completed by badge
            Surface(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text(
                    "✔ COMPLETED BY ${task.mechanicName?.uppercase() ?: "UNKNOWN"}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            // Notes
            if (task.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f))
                ) {
                    Text(
                        task.notes,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            // Accountability: Timestamp
            task.completedAt?.let { timestamp ->
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(24.dp).background(SteelBlue.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            task.mechanicInitials ?: "?",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, fontWeight = FontWeight.Black, color = SteelBlue)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Actioned by: ${task.mechanicName ?: "Unknown"} at ${DateFormat.format("HH:mm", timestamp)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
