package com.example.todovsn.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todovsn.R
import com.example.todovsn.ToDoAppBar
import com.example.todovsn.data.ToDoItem
import com.example.todovsn.ui.AppViewModelProvider
import com.example.todovsn.ui.navigation.NavDestination
import kotlinx.coroutines.launch

object ToDoDetailsDestination : NavDestination {
    override val route = "details"
    override val titleRes = R.string.details
    const val toDoIdArg = "toDoId"
    val routeWithArgs = "$route/{$toDoIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoDetailsScreen(
    navigateToEditToDo: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            ToDoAppBar(
                title = stringResource(ToDoDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateToEditToDo(uiState.value.toDoDetails.id)
                },
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.edit_task),
                    contentDescription = stringResource(R.string.edit_task)
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        ToDoDetailsBody(
            toDoDetailsUiState = uiState.value,
            onToggleCompleted = { viewModel.toggleCompleted() },
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteToDo()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun ToDoDetailsBody(
    toDoDetailsUiState: ToDoDetailsUiState,
    onToggleCompleted: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var deleteConfirmationRequired by rememberSaveable {
        mutableStateOf(false)
    }

    val toDo = toDoDetailsUiState.toDoDetails.toToDo()

    Column(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ToDoDetails(
            toDo = toDo,
            modifier = Modifier.fillMaxWidth()
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = onToggleCompleted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = if (toDo.isCompleted)
                    ButtonDefaults.buttonColors()
                else
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
            ) {
                Icon(
                    painter = if (toDo.isCompleted)
                        painterResource(R.drawable.check_circle)
                    else
                        painterResource(R.drawable.restart_alt),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (toDo.isCompleted)
                        stringResource(R.string.mark_complete)
                    else
                        stringResource(R.string.mark_incomplete),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            OutlinedButton(
                onClick = { deleteConfirmationRequired = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f))
            ) {
                Icon(
                    painter = painterResource(R.drawable.delete_icon),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.delete),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }

        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = {
                    deleteConfirmationRequired = false
                }
            )
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDeleteCancel,
        icon = {
            Icon(
                painter = painterResource(R.drawable.delete_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDeleteConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = "Delete")
            }
        }
    )
}

@Composable
fun ToDoDetails(
    toDo: ToDoItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Status chip
            StatusChip(isCompleted = toDo.isCompleted)

            // Title
            Text(
                text = toDo.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                textDecoration = if (toDo.isCompleted)
                    TextDecoration.LineThrough
                else
                    TextDecoration.None,
                color = if (toDo.isCompleted)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onSurface
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // Description section
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        painterResource(R.drawable.notes),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = toDo.description.ifBlank { "No description added" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (toDo.description.isBlank())
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    else
                        MaterialTheme.colorScheme.onSurface,
                    fontStyle = if (toDo.description.isBlank()) FontStyle.Italic else FontStyle.Normal
                )
            }
        }
    }
}

@Composable
private fun StatusChip(
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isCompleted)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.tertiaryContainer

    val contentColor = if (isCompleted)
        MaterialTheme.colorScheme.onPrimaryContainer
    else
        MaterialTheme.colorScheme.onTertiaryContainer

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(containerColor)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            painter = if (isCompleted){
                painterResource(R.drawable.check_circle)
            } else {
                painterResource(R.drawable.schedule)
            },
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = contentColor
        )
        Text(
            text = if (isCompleted) "Completed" else "Pending",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = contentColor
        )
    }
}