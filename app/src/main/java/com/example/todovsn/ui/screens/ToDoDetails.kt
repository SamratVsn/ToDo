package com.example.todovsn.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        ToDoDetails(
            toDo = toDoDetailsUiState.toDoDetails.toToDo(),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onToggleCompleted,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (toDoDetailsUiState.toDoDetails.isCompleted)
                    stringResource(R.string.mark_incomplete)
                else
                    stringResource(R.string.mark_complete)
            )
        }

        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
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
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "No")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Yes")
            }
        })
}


@Composable
fun ToDoDetails(
    toDo: ToDoItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            ToDoDetailsRow(
                label = "Title",
                value = toDo.title
            )

            ToDoDetailsRow(
                label = "Description",
                value = if (toDo.description.isBlank())
                    "No description"
                else
                    toDo.description
            )

            ToDoDetailsRow(
                label = "Status",
                value = if (toDo.isCompleted)
                    "Completed"
                else
                    "Pending"
            )
        }
    }
}

@Composable
private fun ToDoDetailsRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {

        Text(label)

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = value,
            fontWeight = FontWeight.Bold
        )
    }
}