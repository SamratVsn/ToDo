package com.example.todovsn.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todovsn.R
import com.example.todovsn.ui.AppViewModelProvider
import com.example.todovsn.ui.navigation.NavDestination
import kotlinx.coroutines.launch

object ToDoEditDestination : NavDestination {
    override val route = "todo_edit"
    override val titleRes = R.string.edit_screen
    const val toDoIdArg = "toDoId"
    val routeWithArgs = "$route/{$toDoIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoEditScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val categories by viewModel.categories.collectAsState()

    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        AddToDoBody(
            toDoUiState = viewModel.toDoUiState,
            categories = categories,
            onToDoValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    if (viewModel.updateToDo()) {
                        navigateBack()
                    }
                }
            },
            onBackClick = navigateBack,
            mode = TaskScreenMode.EDIT,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}
