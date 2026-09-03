package com.example.todovsn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todovsn.R
import com.example.todovsn.data.Category
import com.example.todovsn.data.preference.ThemeMode
import com.example.todovsn.ui.AppViewModelProvider
import com.example.todovsn.ui.components.EditNameDialog
import com.example.todovsn.ui.navigation.NavDestination

object SettingsDestination : NavDestination {
    override val route = "settings"
    override val titleRes = R.string.settings
}

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val uiState by settingsViewModel.uiState.collectAsState()

    SettingsContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onNameClick = { settingsViewModel.showNameDialog(true) },
        onThemeClick = { settingsViewModel.showThemeDialog(true) },
        onCategoryClick = { settingsViewModel.showCategoryManagement(true) },
        onResetClick = { settingsViewModel.showResetConfirmation(true) },
        onDeleteClick = { settingsViewModel.showDeleteConfirmation(true) },
        onReminderToggle = { settingsViewModel.setSmartRemindersEnabled(it) },
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

    if (uiState.isCategoryManagementOpen) {
        CategoryManagementDialog(
            categories = uiState.categories,
            onAdd = settingsViewModel::addCategory,
            onRemove = settingsViewModel::deleteCategory,
            onDismiss = { settingsViewModel.showCategoryManagement(false) }
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
    onBackClick: () -> Unit,
    onNameClick: () -> Unit,
    onThemeClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onResetClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReminderToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colorScheme.background
    ) { innerPadding: PaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            colorScheme.surfaceVariant,
                            CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = "Back",
                        modifier = Modifier.size(20.dp),
                        tint = colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = CircleShape,
                    color = colorScheme.surfaceVariant,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(R.drawable.settings),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            SettingsSectionHeader("PROFILE")
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = colorScheme.surfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, colorScheme.outline.copy(alpha = 0.1f))
            ) {
                Column {
                    SettingsItem(
                        icon = R.drawable.profile,
                        title = "Display Name",
                        subtitle = uiState.preferences.displayName,
                        onClick = onNameClick
                    )
                    SettingsItem(
                        icon = R.drawable.settings, // Use an appearance icon if available
                        title = "Appearance",
                        subtitle = when (uiState.preferences.themeMode) {
                            ThemeMode.SYSTEM -> "System Default"
                            ThemeMode.LIGHT -> "Light"
                            ThemeMode.DARK -> "Deep Sea (Dark)"
                        },
                        onClick = onThemeClick
                    )
                    SettingsItem(
                        icon = R.drawable.category,
                        title = "Categories",
                        subtitle = "Manage your task categories",
                        onClick = onCategoryClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Preferences Group ---
            SettingsSectionHeader("PREFERENCES")
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = colorScheme.surfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, colorScheme.outline.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = colorScheme.primary.copy(alpha = 0.1f),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(R.drawable.schedule), // Replacement for reminder icon
                                contentDescription = null,
                                modifier = Modifier.size(22.dp),
                                tint = colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Smart Reminders",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = uiState.preferences.smartRemindersEnabled,
                        onCheckedChange = onReminderToggle,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = colorScheme.primary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Data Management Group ---
            SettingsSectionHeader("DATA MANAGEMENT")
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = colorScheme.surfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, colorScheme.outline.copy(alpha = 0.1f))
            ) {
                Column {
                    SettingsItem(
                        icon = R.drawable.restart_alt,
                        title = "Reset Counter",
                        subtitle = "Reset lifetime task counter",
                        onClick = onResetClick
                    )
                    SettingsItem(
                        icon = R.drawable.delete_icon,
                        title = "Delete All Data",
                        subtitle = "Clear all tasks and preferences",
                        titleColor = colorScheme.error,
                        onClick = onDeleteClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- About Group ---
            SettingsSectionHeader("ABOUT")
            AboutAppCard()
            
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "© 2026 Samrat Parajuli",
                style = MaterialTheme.typography.labelSmall,
                color = colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun SettingsSectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        letterSpacing = 1.5.sp,
        modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
    )
}

@Composable
private fun SettingsItem(
    icon: Int,
    title: String,
    subtitle: String,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = titleColor
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                painter = painterResource(R.drawable.arrow_back), // Use a chevron if available
                contentDescription = null,
                modifier = Modifier.size(16.dp).background(Color.Transparent).offset(x = 8.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun AboutAppCard() {
    val colorScheme = MaterialTheme.colorScheme
    val uriHandler = LocalUriHandler.current
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = colorScheme.surfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, colorScheme.outline.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🚀", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "About Tasks",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "A sleek, productivity-focused manager designed to help you stay organized and reach your goals. Built with love and high-end focus.",
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.onSurfaceVariant,
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "BUILT WITH",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TechTag("Kotlin")
                TechTag("Compose")
                TechTag("Room")
                TechTag("MVVM")
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = androidx.compose.ui.Modifier.clickable {
                        uriHandler.openUri("https://github.com/SamratVsn")
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.share),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Source Code",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    )
                }
                Text(
                    text = "v1.1.0",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun TechTag(name: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun CategoryManagementDialog(
    categories: List<Category>,
    onAdd: (String) -> Unit,
    onRemove: (Category) -> Unit,
    onDismiss: () -> Unit
) {
    var newCategoryName by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Manage Categories") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Add new category
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { name -> newCategoryName = name },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("New category...") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (newCategoryName.isNotBlank()) {
                                onAdd(newCategoryName)
                                newCategoryName = ""
                            }
                        },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.add),
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                HorizontalDivider()

                // List existing categories
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { category ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = category.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                IconButton(onClick = { onRemove(category) }) {
                                    Icon(
                                        painter = painterResource(R.drawable.delete_icon),
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
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
