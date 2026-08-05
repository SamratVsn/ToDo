package com.example.todovsn.ui.screens

import androidx.compose.runtime.Composable
import com.example.todovsn.R
import com.example.todovsn.ui.navigation.NavDestination

object ToDoDetailsDestination : NavDestination {
    override val route = "details"
    override val titleRes = R.string.details
    const val toDoIdArg = "toDoId"
    val routeWithArgs = "$route/{$toDoIdArg}"
}

@Composable
fun ToDoDetailsScreen(){

}