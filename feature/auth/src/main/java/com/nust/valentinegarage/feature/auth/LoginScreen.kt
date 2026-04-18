package com.nust.valentinegarage.feature.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nust.valentinegarage.core.ui.R
import androidx.compose.ui.tooling.preview.Preview
import com.nust.valentinegarage.core.ui.components.ValentinePrimaryButton
import com.nust.valentinegarage.core.ui.components.ValentineTextField

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (role: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Handle navigation/success
    LaunchedEffect(uiState.successUser) {
        uiState.successUser?.let {
            onLoginSuccess(it.role.name)
        }
    }

    LoginScreenContent(
        uiState = uiState,
        onLoginClicked = viewModel::onLoginClicked,
        onForgotPasswordClicked = viewModel::onForgotPasswordClicked,
        clearSnackbar = viewModel::clearSnackbar
    )
}

@Composable
fun LoginScreenContent(
    uiState: LoginUiState,
    onLoginClicked: (String, String) -> Unit,
    onForgotPasswordClicked: (String) -> Unit,
    clearSnackbar: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle Snackbars
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            clearSnackbar()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Valentine Garage Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Titles
            Text(
                text = "VALENTINE'S GARAGE",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Text(
                text = "PRECISION DIAGNOSTICS & REPAIR PORTAL",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Inputs
            ValentineTextField(
                value = email,
                onValueChange = { email = it },
                label = "TECHNICIAN EMAIL",
                leadingIcon = Icons.Default.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "FORGOT PASSWORD?",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clickable { onForgotPasswordClicked(email) }
                )
                ValentineTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "SECURITY KEY",
                    leadingIcon = Icons.Default.Lock,
                    visualTransformation = PasswordVisualTransformation()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            if (uiState.isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else {
                ValentinePrimaryButton(
                    text = "INITIALIZE SESSION ➔",
                    onClick = { onLoginClicked(email, password) }
                )
            }

            // Error Display
            uiState.error?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    com.nust.valentinegarage.core.ui.theme.ValentineGarageTheme {
        LoginScreenContent(
            uiState = LoginUiState(error = "MOCK ERROR: DATABASE OFFLINE"),
            onLoginClicked = { _, _ -> },
            onForgotPasswordClicked = {},
            clearSnackbar = {}
        )
    }
}

