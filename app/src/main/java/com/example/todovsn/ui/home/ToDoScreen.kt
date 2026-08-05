package com.example.todovsn.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todovsn.R
import com.example.todovsn.ToDoAppBar
import com.example.todovsn.data.ToDoItem
import com.example.todovsn.ui.AppViewModelProvider
import com.example.todovsn.ui.navigation.NavDestination
import com.example.todovsn.ui.theme.ToDoVsnTheme

object HomeDestination : NavDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen(
    onCheckedChange: (Int, Boolean) -> Unit,
    onDelete: (Int) -> Unit,
    navigateToTaskEntry: () -> Unit,
    navigateToTaskUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ToDoAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToTaskEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.add_task),
                    contentDescription = stringResource(R.string.add_screen),
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            toDoList = homeUiState.toDoList,
            onToDoClick = navigateToTaskUpdate,
            onCheckedChange = onCheckedChange,
            modifier = modifier.fillMaxSize(),
            onDelete = onDelete,
            contentPadding = innerPadding
        )
    }
}

@Composable
private fun HomeBody(
    toDoList: List<ToDoItem>,
    onToDoClick: (Int) -> Unit,
    onCheckedChange: (Int, Boolean) -> Unit,
    onDelete: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
){
    if(toDoList.isEmpty()){
        EmptyScreen(
            modifier = modifier.padding(contentPadding)
        )
    }
    else{
        ToDoList(
            toDoList = toDoList,
            onToDoClick = { onToDoClick(it.id) },
            contentPadding = contentPadding,
            onDelete = onDelete,
            onCheckedChange = onCheckedChange,
            modifier = modifier.padding(horizontal = 8.dp),
        )
    }
}

@Composable
private fun EmptyScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.check_circle),
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.outline
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.no_new_yet),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.no_task),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ToDoList(
    toDoList: List<ToDoItem>,
    onToDoClick: (ToDoItem) -> Unit,
    onCheckedChange: (Int, Boolean) -> Unit,
    onDelete: (Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
){
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        items(items = toDoList, key = {it.id}) { item ->
            ToDoCard(
                toDo = item,
                onCheckedChange = onCheckedChange,
                onDelete = onDelete,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onToDoClick(item) })
        }
    }
}
@Composable
private fun ToDoCard(
    onCheckedChange: (Int, Boolean) -> Unit,
    onDelete: (Int) -> Unit,
    toDo: ToDoItem,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Checkbox(
                checked = toDo.isCompleted,
                onCheckedChange = {
                    onCheckedChange(toDo.id, it)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = toDo.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration =
                        if (toDo.isCompleted)
                            TextDecoration.LineThrough
                        else
                            TextDecoration.None
                ),
                color = if (toDo.isCompleted)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )


            IconButton(
                onClick = {
                    onDelete(toDo.id)
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.delete_icon),
                    contentDescription = "Delete Task"
                )
            }
        }
    }
}
