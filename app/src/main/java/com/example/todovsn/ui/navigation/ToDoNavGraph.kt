package com.example.todovsn.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todovsn.ui.home.HomeDestination
import com.example.todovsn.ui.screens.AddToDoDestination
import com.example.todovsn.ui.screens.SettingDestination
import com.example.todovsn.ui.screens.ToDoDetailsDestination

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

        }
        composable(route = AddToDoDestination.route) {

        }
        composable(route = ToDoDetailsDestination.route) {

        }
        composable(route = SettingDestination.route) {

        }
    }
}