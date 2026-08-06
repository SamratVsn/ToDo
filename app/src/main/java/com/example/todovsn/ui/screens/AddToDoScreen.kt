package com.example.todovsn.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToDoScreen(
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
                modifier = Modifier
                    .padding(
                        start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                        end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                        top = innerPadding.calculateTopPadding()
                    )
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
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ToDoInputForm(
            toDoDetails = toDoUiState.toDoDetails,
            onValueChange = onToDoValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled =toDoUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
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
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = toDoDetails.title,
            onValueChange = {
                onValueChange(toDoDetails.copy(title = it))
            },
            label = { Text("ToDo Title*") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        )

        OutlinedTextField(
            value = toDoDetails.description,
            onValueChange = {
                onValueChange(toDoDetails.copy(description = it))
            },
            label = { Text("ToDo Description") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp),
            enabled = enabled,
            maxLines = 5,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        )

        if (enabled) {
            Text(
                text = "* Required field",
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}