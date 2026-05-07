package com.nust.valentinegarage.feature.admin

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
import android.content.Context
import android.content.Intent
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nust.valentinegarage.core.model.CheckIn
import com.nust.valentinegarage.core.model.Task
import com.nust.valentinegarage.core.model.TaskPriority
import com.nust.valentinegarage.core.model.TaskStatus
import com.nust.valentinegarage.core.ui.theme.IndustrialOrange
import com.nust.valentinegarage.core.ui.theme.SteelBlue
import com.nust.valentinegarage.core.ui.theme.SuccessGreen
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditTrailScreen(
    checkInId: String,
    viewModel: AuditViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(checkInId) {
        viewModel.loadAudit(checkInId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "AUDIT: ${uiState.checkIn?.vehicleId ?: ""}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        uiState.checkIn?.let { checkIn ->
                            printAuditReport(context, checkIn, uiState.tasks)
                        }
                    }) {
                        Icon(Icons.Default.Print, contentDescription = "Print")
                    }
                    IconButton(onClick = {
                        uiState.checkIn?.let { checkIn ->
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, generateAuditReportText(checkIn, uiState.tasks))
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Audit Report"))
                        }
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Success Banner if cleared
            if (uiState.vehicleCleared) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = SuccessGreen
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "VEHICLE CLEARED",
                                color = Color.White,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black)
                            )
                        }
                    }
                }
            }

            // Header with check-in details
            item {
                uiState.checkIn?.let { checkIn ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            val vehicleHeader = if (checkIn.vehicleModel.isNotBlank()) {
                                "${checkIn.vehicleId} - ${checkIn.vehicleModel}"
                            } else {
                                checkIn.vehicleId
                            }
                            Text(
                                text = vehicleHeader.uppercase(),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = IndustrialOrange)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("ARRIVED", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(DateFormat.format("HH:mm", checkIn.timestamp).toString(), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Black))
                                }
                                Column {
                                    Text("CHECKED IN BY", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(checkIn.checkedInBy, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Black))
                                }
                                // Date box
                                Surface(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(4.dp),
                                    shadowElevation = 1.dp
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            DateFormat.format("dd", checkIn.timestamp).toString(),
                                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black)
                                        )
                                        Text(
                                            DateFormat.format("MMM", checkIn.timestamp).toString().uppercase(),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("ODOMETER", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("${checkIn.kilometersDriven} KM", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                }
                                Column(modifier = Modifier.weight(2f)) {
                                    Text("INITIAL CONDITION", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(checkIn.initialCondition.ifBlank { "N/A" }, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                }
                            }
                        }
                    }
                }
            }

            // Audit Timeline header
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Audit Timeline",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Surface(
                        color = SuccessGreen.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            "${uiState.verifiedCount} TASKS VERIFIED",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = SuccessGreen
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Task audit cards
            if (uiState.tasks.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text("No verified tasks yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                items(uiState.tasks) { task ->
                    AuditTaskCard(task = task)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun AuditTaskCard(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Task Name with orange checkmark
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = IndustrialOrange, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    task.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Accountability Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(28.dp).background(SteelBlue.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        task.mechanicInitials ?: "?",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, color = SteelBlue, fontSize = 10.sp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    buildString {
                        append("Actioned by: ${task.mechanicName ?: "Unknown"}")
                        task.completedAt?.let { time ->
                            append(" at ${DateFormat.format("HH:mm", time)}")
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Diagnostic notes in pale yellow box
            if (task.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f))
                ) {
                    Text(
                        task.notes,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

fun generateAuditReportText(checkIn: com.nust.valentinegarage.core.model.CheckIn, tasks: List<Task>): String {
    val sb = StringBuilder()
    sb.append("VALENTINE'S GARAGE - AUDIT REPORT\n")
    sb.append("================================\n")
    sb.append("Vehicle: ${checkIn.vehicleId}\n")
    sb.append("Date: ${DateFormat.format("dd/MM/yyyy HH:mm", checkIn.timestamp)}\n")
    sb.append("Mechanic: ${checkIn.checkedInBy}\n")
    sb.append("Odometer: ${checkIn.kilometersDriven} KM\n")
    sb.append("Initial Condition: ${checkIn.initialCondition.ifBlank { "N/A" }}\n")
    sb.append("\nTASKS EXECUTED:\n")
    if (tasks.isEmpty()) {
        sb.append("No verified tasks yet.\n")
    } else {
        tasks.forEach { task ->
            sb.append("- ${task.name} (By: ${task.mechanicName})\n")
            if (task.notes.isNotBlank()) {
                sb.append("  Notes: ${task.notes}\n")
            }
        }
    }
    sb.append("\n================================\n")
    return sb.toString()
}

// Keep a reference so it's not garbage collected while printing
private var activeWebView: WebView? = null

fun printAuditReport(context: Context, checkIn: com.nust.valentinegarage.core.model.CheckIn, tasks: List<Task>) {
    val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager ?: return
    val jobName = "Audit_${checkIn.vehicleId}"
    val webView = WebView(context)
    activeWebView = webView
    
    val html = """
        <html>
        <head>
            <style>
                body { font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; padding: 20px; color: #333; }
                h1 { color: #E65100; text-align: center; font-weight: 900; }
                h2 { color: #555; border-bottom: 2px solid #eee; padding-bottom: 5px; }
                .info-box { background-color: #f9f9f9; padding: 15px; border-radius: 8px; margin-bottom: 20px; border: 1px solid #e0e0e0; }
                .info-row { display: flex; justify-content: space-between; margin-bottom: 8px; font-size: 14px; }
                .info-label { font-weight: bold; color: #777; }
                .info-value { font-weight: bold; }
                .task-item { border-left: 4px solid #4CAF50; padding-left: 15px; margin-bottom: 20px; page-break-inside: avoid; }
                .task-title { font-weight: bold; font-size: 16px; margin-bottom: 5px; }
                .task-meta { font-size: 12px; color: #777; margin-bottom: 8px; }
                .task-notes { background-color: #FFFDE7; padding: 10px; border-radius: 4px; font-style: italic; font-size: 13px; color: #5D4037; border: 1px solid #FFF9C4; }
                .footer { margin-top: 40px; text-align: center; font-size: 10px; color: #aaa; border-top: 1px solid #eee; padding-top: 10px; }
            </style>
        </head>
        <body>
            <h1>VALENTINE'S GARAGE</h1>
            <h2 style="text-align: center; border: none;">OFFICIAL AUDIT REPORT</h2>
            
            <div class="info-box">
                <div class="info-row"><span class="info-label">VEHICLE ID:</span> <span class="info-value">${checkIn.vehicleId}</span></div>
                <div class="info-row"><span class="info-label">DATE:</span> <span class="info-value">${DateFormat.format("dd/MM/yyyy HH:mm", checkIn.timestamp)}</span></div>
                <div class="info-row"><span class="info-label">CHECKED IN BY:</span> <span class="info-value">${checkIn.checkedInBy}</span></div>
                <div class="info-row"><span class="info-label">ODOMETER:</span> <span class="info-value">${checkIn.kilometersDriven} KM</span></div>
                <div class="info-row"><span class="info-label">INITIAL CONDITION:</span> <span class="info-value">${checkIn.initialCondition.ifBlank { "N/A" }}</span></div>
                <div class="info-row"><span class="info-label">STATUS:</span> <span class="info-value">${if (checkIn.isCompleted) "CLEARED" else "ACTIVE"}</span></div>
            </div>
            
            <h2>EXECUTED TASKS (${tasks.size})</h2>
            ${if (tasks.isEmpty()) "<p style='color: #777;'>No tasks recorded for this vehicle.</p>" else ""}
            ${tasks.joinToString("") { task ->
                val completedTime = task.completedAt?.let { DateFormat.format("HH:mm", it).toString() } ?: ""
                val timeString = if (completedTime.isNotEmpty()) "at $completedTime" else ""
                """
                <div class="task-item">
                    <div class="task-title">✓ ${task.name}</div>
                    <div class="task-meta">Actioned by: <b>${task.mechanicName}</b> $timeString</div>
                    ${if (task.notes.isNotBlank()) "<div class='task-notes'>\"${task.notes}\"</div>" else ""}
                </div>
                """
            }}
            
            <div class="footer">
                Generated by Valentine's Garage Digital Portal on ${DateFormat.format("dd/MM/yyyy HH:mm", System.currentTimeMillis())}
            </div>
        </body>
        </html>
    """.trimIndent()
    
    webView.webViewClient = object : android.webkit.WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            val printAdapter = webView.createPrintDocumentAdapter(jobName)
            printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
        }
    }
    
    webView.loadDataWithBaseURL(null, html, "text/HTML", "UTF-8", null)
}
