package com.example.todovsn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todovsn.R
import com.example.todovsn.data.preference.ThemeMode
import com.example.todovsn.data.preference.UserPreferences
import com.example.todovsn.ui.AppViewModelProvider
import com.example.todovsn.ui.navigation.NavDestination

object SettingsDestination : NavDestination {
    override val route = "settings"
    override val titleRes = R.string.settings
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val uiState by settingsViewModel.uiState.collectAsState()

    SettingsContent(
        uiState = uiState,
        onNameClick = { settingsViewModel.showNameDialog(true) },
        onThemeClick = { settingsViewModel.showThemeDialog(true) },
        onResetClick = { settingsViewModel.showResetConfirmation(true) },
        onDeleteClick = { settingsViewModel.showDeleteConfirmation(true) },
        modifier = modifier
    )

    if (uiState.isNameDialogOpen) {
        EditNameDialog(
            initialName = uiState.preferences.displayName,
            onConfirm = settingsViewModel::updateDisplayName,
            onDismiss = { settingsViewModel.showNameDialog(false) }
        )
    }

    if (uiState.isThemeDialogOpen) {
        ThemeSelectionDialog(
            currentTheme = uiState.preferences.themeMode,
            onThemeSelected = settingsViewModel::setThemeMode,
            onDismiss = { settingsViewModel.showThemeDialog(false) }
        )
    }

    if (uiState.isResetConfirmationOpen) {
        ConfirmationDialog(
            title = "Reset Progress",
            text = "This will reset your total task counter to 0. This cannot be undone.",
            onConfirm = settingsViewModel::resetTotalTasks,
            onDismiss = { settingsViewModel.showResetConfirmation(false) }
        )
    }

    if (uiState.isDeleteConfirmationOpen) {
        ConfirmationDialog(
            title = "Delete All Data",
            text = "This will permanently delete all your tasks and reset settings. This action is irreversible.",
            confirmColor = MaterialTheme.colorScheme.error,
            onConfirm = settingsViewModel::deleteAllData,
            onDismiss = { settingsViewModel.showDeleteConfirmation(false) }
        )
    }
}

@Composable
private fun SettingsContent(
    uiState: SettingsUiState,
    onNameClick: () -> Unit,
    onThemeClick: () -> Unit,
    onResetClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorScheme.surface,
                        colorScheme.surfaceVariant
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            SettingsSectionHeader("PROFILE")
            SettingsItem(
                icon = painterResource(R.drawable.profile),
                title = "Display Name",
                subtitle = uiState.preferences.displayName,
                onClick = onNameClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSectionHeader("APPEARANCE")
            SettingsItem(
                icon = painterResource(R.drawable.settings),
                title = "Theme",
                subtitle = when (uiState.preferences.themeMode) {
                    ThemeMode.SYSTEM -> "System Default"
                    ThemeMode.LIGHT -> "Light"
                    ThemeMode.DARK -> "Dark"
                },
                onClick = onThemeClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSectionHeader("DATA MANAGEMENT")
            SettingsItem(
                icon = painterResource(R.drawable.restart_alt),
                title = "Reset Counter",
                subtitle = "Reset lifetime (total) task counter",
                onClick = onResetClick
            )
            SettingsItem(
                icon = painterResource(R.drawable.delete_icon),
                title = "Delete All Data",
                subtitle = "Clear all tasks and preferences",
                onClick = onDeleteClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSectionHeader("ABOUT")
            SettingsItem(
                icon = painterResource(R.drawable.info),
                title = "About Tasks",
                subtitle = "Learn more about the app",
                onClick = { uriHandler.openUri("https://www.samratparajuli0.com.np/projects/todo") }
            )
            SettingsItem(
                icon = painterResource(R.drawable.notes),
                title = "Version",
                subtitle = "1.1.0",
                onClick = { }
            )
            SettingsItem(
                icon = painterResource(android.R.drawable.ic_menu_share),
                title = "GitHub",
                subtitle = "View source code",
                onClick = { uriHandler.openUri("https://github.com/SamratVsn") }
            )

            Spacer(modifier = Modifier.height(48.dp))
            HorizontalDivider(color = colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(32.dp))

            AboutAppCard()
            Spacer(modifier = Modifier.height(16.dp))
            BuiltWithCard()
            
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "© 2026 Samrat Parajuli",
                fontSize = 12.sp,
                color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun SettingsSectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
    )
}

@Composable
private fun SettingsItem(
    icon: Painter,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "›",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
private fun AboutAppCard() {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "🚀 About Tasks",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tasks is a sleek, productivity-focused task manager designed to help you stay organized and reach your goals. It features a modern user interface with dark mode support, persistent task tracking, and intuitive swipe gestures for a seamless experience.",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BuiltWithCard() {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "🛠 Built With",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            val techs = listOf("Kotlin", "Compose", "Room", "DataStore", "Coroutines", "Navigation", "MVVM")
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                techs.forEach { tech ->
                    Surface(
                        color = colorScheme.primaryContainer.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = tech,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeSelectionDialog(
    currentTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Theme") },
        text = {
            Column {
                ThemeOption("System Default", ThemeMode.SYSTEM, currentTheme, onThemeSelected)
                ThemeOption("Light", ThemeMode.LIGHT, currentTheme, onThemeSelected)
                ThemeOption("Dark", ThemeMode.DARK, currentTheme, onThemeSelected)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

@Composable
private fun ThemeOption(
    label: String,
    mode: ThemeMode,
    selectedMode: ThemeMode,
    onClick: (ThemeMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = (mode == selectedMode),
            onClick = { onClick(mode) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun ConfirmationDialog(
    title: String,
    text: String,
    confirmColor: Color = MaterialTheme.colorScheme.primary,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm", color = confirmColor)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
