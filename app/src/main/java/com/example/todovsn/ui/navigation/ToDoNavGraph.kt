package com.example.todovsn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todovsn.ui.home.HomeDestination
import com.example.todovsn.ui.home.ToDoScreen
import com.example.todovsn.ui.screens.AddToDoDestination
import com.example.todovsn.ui.screens.AddToDoScreen
import com.example.todovsn.ui.screens.SettingsDestination
import com.example.todovsn.ui.screens.SettingsScreen
import com.example.todovsn.ui.screens.ProfileDestination
import com.example.todovsn.ui.screens.ProfileScreen
import com.example.todovsn.ui.screens.ToDoDetailsDestination
import com.example.todovsn.ui.screens.ToDoDetailsScreen
import com.example.todovsn.ui.screens.ToDoEditDestination
import com.example.todovsn.ui.screens.ToDoEditScreen

@Composable
fun ToDoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ){
        composable(route = HomeDestination.route) {
            ToDoScreen(
                navigateToTaskEntry = {
                    navController.navigate(AddToDoDestination.route)
                },
                navigateToTaskUpdate = {
                    navController.navigate("${ToDoDetailsDestination.route}/${it}")
                }
            )
        }

        composable(route = AddToDoDestination.route) {
            AddToDoScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = ToDoDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ToDoDetailsDestination.toDoIdArg) {
                type = NavType.IntType
            })
        ) {
            ToDoDetailsScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToEditToDo = {
                    navController.navigate("${ToDoEditDestination.route}/$it")
                }
            )
        }

        composable(
            route = ToDoEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ToDoEditDestination.toDoIdArg) {
                type = NavType.IntType
            })
        ) {
            ToDoEditScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = SettingsDestination.route) {
            SettingsScreen()
        }

        composable(route = ProfileDestination.route) {
            ProfileScreen()
        }
    }
}