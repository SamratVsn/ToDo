package com.example.todovsn.ui.screens

import androidx.compose.runtime.Composable
import com.example.todovsn.R
import com.example.todovsn.ui.navigation.NavDestination

object ToDoEditDestination : NavDestination {
    override val route = "todo_edit"
    override val titleRes = R.string.edit_screen
    const val toDoIdArg = "toDoId"
    val routeWithArgs = "$route/{$toDoIdArg}"
}

@Composable
fun ToDoEditScreen(){

}