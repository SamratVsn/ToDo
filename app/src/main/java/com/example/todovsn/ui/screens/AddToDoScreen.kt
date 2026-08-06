package com.example.todovsn.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todovsn.R
import com.example.todovsn.ToDoAppBar
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToDoScreen(
    mode: TaskScreenMode = TaskScreenMode.ADD,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: AddViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            ToDoAppBar(
                title = stringResource(AddToDoDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
            )
        }
    ){ innerPadding ->
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
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = when (mode) {
                    TaskScreenMode.ADD -> "New Task"
                    TaskScreenMode.EDIT -> "Edit Task"
                }
            )
            Text(
                text = "Add/Edit the details for your task below",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        ToDoInputForm(
            toDoDetails = toDoUiState.toDoDetails,
            onValueChange = onToDoValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = onSaveClick,
            enabled = toDoUiState.isEntryValid,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.add_task),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = when (mode) {
                    TaskScreenMode.ADD -> "Add Task"
                    TaskScreenMode.EDIT -> "Save Changes"
                },
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
private fun ToDoInputForm(
    toDoDetails: ToDoDetails,
    modifier: Modifier = Modifier,
    onValueChange: (ToDoDetails) -> Unit = {},
    enabled: Boolean = true
) {
    val titleMax = 60
    val descMax = 250

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            OutlinedTextField(
                value = toDoDetails.title,
                onValueChange = {
                    if (it.length <= titleMax) onValueChange(toDoDetails.copy(title = it))
                },
                label = { Text("Title") },
                placeholder = { Text("e.g. Finish project report") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.edit_task),
                        contentDescription = null
                    )
                },

                trailingIcon = {
                    if (toDoDetails.title.isNotEmpty()) {
                        IconButton(onClick = { onValueChange(toDoDetails.copy(title = "")) }) {
                            Icon(
                                painter = painterResource(R.drawable.delete_icon),
                                contentDescription = "Clear title"
                            )
                        }
                    }
                },
                supportingText = {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (toDoDetails.title.isNotEmpty() && toDoDetails.title.trim().length < 3) {
                            Text(
                                text = "Min 3 characters",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        Text(
                            text = "${toDoDetails.title.length}/$titleMax",
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.End
                        )
                    }
                },
                singleLine = true,
                enabled = enabled,
                isError = toDoDetails.title.isNotEmpty() && toDoDetails.title.trim().length < 3,
                shape = RoundedCornerShape(14.dp),
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Description field
        OutlinedTextField(
            value = toDoDetails.description,
            onValueChange = {
                if (it.length <= descMax) onValueChange(toDoDetails.copy(description = it))
            },
            label = { Text("Description") },
            placeholder = { Text("Add notes or details (optional)") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.notes),
                    contentDescription = null,
                    modifier = Modifier.padding(bottom = 60.dp) // aligns icon to top
                )
            },
            supportingText = {
                Text(
                    text = "${toDoDetails.description.length}/$descMax",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            },
            enabled = enabled,
            minLines = 4,
            maxLines = 6,
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        if (enabled) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painterResource(R.drawable.info),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Title is required (min 3 characters)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}