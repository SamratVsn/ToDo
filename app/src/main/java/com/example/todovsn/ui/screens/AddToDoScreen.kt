package com.example.todovsn.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todovsn.R
import com.example.todovsn.ui.AppViewModelProvider
import com.example.todovsn.ui.navigation.NavDestination
import kotlinx.coroutines.launch

object AddToDoDestination : NavDestination {
    override val route = "item_entry"
    override val titleRes = R.string.add_screen
}

enum class TaskScreenMode {
    ADD,
    EDIT
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToDoScreen(
    mode: TaskScreenMode = TaskScreenMode.ADD,
    navigateBack: () -> Unit,
    viewModel: AddViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold { innerPadding ->
        AddToDoBody(
            toDoUiState = viewModel.toDoUiState,
            onToDoValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveToDo()
                    navigateBack()
                }
            },
            mode = mode,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddToDoBody(
    toDoUiState: ToDoUiState,
    onToDoValueChange: (ToDoDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    mode: TaskScreenMode,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 24.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Header
        Text(
            text = when (mode) {
                TaskScreenMode.ADD -> "New Task"
                TaskScreenMode.EDIT -> "Edit Task"
            },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        ToDoInputForm(
            toDoDetails = toDoUiState.toDoDetails,
            onValueChange = onToDoValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSaveClick,
            enabled = toDoUiState.isEntryValid,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = when (mode) {
                    TaskScreenMode.ADD -> "Add Task"
                    TaskScreenMode.EDIT -> "Save Changes"
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToDoInputForm(
    toDoDetails: ToDoDetails,
    modifier: Modifier = Modifier,
    onValueChange: (ToDoDetails) -> Unit = {},
    enabled: Boolean = true
) {
    val titleMax = 60
    val descMax = 500

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        errorBorderColor = Color.Transparent,
        errorContainerColor = Color.Transparent
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Task Title Section
        Column {
            Text(
                text = "Task title",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedTextField(
                value = toDoDetails.title,
                onValueChange = {
                    if (it.length <= titleMax) onValueChange(toDoDetails.copy(title = it))
                },
                placeholder = { Text("e.g. Complete Movie App README") },
                singleLine = true,
                enabled = enabled,
                isError = toDoDetails.title.isNotEmpty() && toDoDetails.title.trim().length < 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = if (toDoDetails.title.isNotEmpty() && toDoDetails.title.trim().length < 3)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.outlineVariant
            )
            if (toDoDetails.title.isNotEmpty() && toDoDetails.title.trim().length < 3) {
                Text(
                    text = "Min 3 characters",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Category Section
        Column {
            Text(
                text = "Category",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            var expanded by remember { mutableStateOf(false) }
            val categories = listOf("Study", "Development", "Routine", "Extra", "Productive", "Important")

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clickable { expanded = true }
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = toDoDetails.category,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        painter = painterResource(android.R.drawable.arrow_down_float),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                onValueChange(toDoDetails.copy(category = category))
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        // Description Section
        Column {
            Text(
                text = "Description",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedTextField(
                value = toDoDetails.description,
                onValueChange = {
                    if (it.length <= descMax) onValueChange(toDoDetails.copy(description = it))
                },
                placeholder = { Text("What needs to be done?") },
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}
