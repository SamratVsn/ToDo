package com.example.todovsn.ui.screens

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todovsn.R
import com.example.todovsn.model.ToDoItem
import com.example.todovsn.ui.ToDoViewModel
import com.example.todovsn.ui.theme.ToDoVsnTheme

@Composable
fun ToDoScreen(
    toDoItems: List<ToDoItem>,
    onEditClicked: () -> Unit,
    viewModel: ToDoViewModel,
    modifier: Modifier = Modifier
){
    if(toDoItems.isEmpty()){
        EmptyScreen()
    }
    else{
        ToDo(
            items = toDoItems,
            onEditClicked = onEditClicked,
            viewModel = viewModel,
            modifier = modifier,
        )
    }
}

@Composable
fun EmptyScreen(
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
                text = "No tasks yet",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Tap + to create your first task.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ToDo(
    items: List<ToDoItem>,
    onEditClicked: () -> Unit,
    viewModel: ToDoViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = { it.id }) { item ->
            ToDoBox(
                item = item,
                onCheckedChange = { viewModel.checkToDo(item.id) },
                onEdit = onEditClicked,
                onDelete = { viewModel.deleteToDo(item.id) }
            )
        }
    }
}

@Composable
private fun ToDoBox(
    item: ToDoItem,
    onCheckedChange: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration =
                        if (item.isCompleted)
                            TextDecoration.LineThrough
                        else
                            TextDecoration.None
                ),
                color = if (item.isCompleted)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onEdit,
            ) {
                Icon(
                    painter = painterResource(R.drawable.edit_task),
                    contentDescription = "Edit Task"
                )
            }

            IconButton(
                onClick = onDelete,
            ) {
                Icon(
                    painter = painterResource(R.drawable.delete_icon),
                    contentDescription = "Delete Task"
                )
            }
        }
    }
}

@Preview
@Composable
private fun ToDoBoxPreview(){
    ToDoVsnTheme {
        ToDoBox(
            item = ToDoItem(
                id = 1,
                title = "Hello",
                isCompleted = false
            ),
            onCheckedChange = {},
            onEdit = {},
            onDelete = {},
            modifier = Modifier
        )
//        EmptyScreen()
    }
}