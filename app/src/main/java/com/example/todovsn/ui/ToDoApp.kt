package com.example.todovsn.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todovsn.R
import com.example.todovsn.ui.screens.AddEditScreen
import com.example.todovsn.ui.screens.SettingsScreen
import com.example.todovsn.ui.screens.ToDoScreen

enum class ToDoScreens(@StringRes val title: Int){
    Home(title = R.string.app_name),
    AddEdit(title = R.string.add_screen),
    Settings(title = R.string.settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToDoAppBar(
    currentScreen: ToDoScreens,
    canNavigate: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
){
    TopAppBar(
        title = {Text(stringResource(currentScreen.title))},
        modifier = modifier,
        navigationIcon = {
            if(canNavigate){
                IconButton( onClick = navigateUp) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = stringResource(R.string.arrow_back),
                    )
                }
            }
        }
    )
}

@Composable
fun ToDoApp(
    viewModel: ToDoViewModel = viewModel(),
){
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = ToDoScreens.valueOf(
        backStackEntry?.destination?.route ?: ToDoScreens.Home.name
    )

    Scaffold(
        topBar = {
            ToDoAppBar(
                currentScreen = currentScreen,
                canNavigate = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = ToDoScreens.Home.name,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = ToDoScreens.Home.name) {
                ToDoScreen(
                    onNewClick = { navController.navigate(ToDoScreens.AddEdit.name) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = ToDoScreens.AddEdit.name) {
                AddEditScreen(
                    onBackPressed = { navController.navigate(ToDoScreens.Home.name) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = ToDoScreens.Settings.name) {
                SettingsScreen(
                    onBackPressed = { navController.navigate(ToDoScreens.Home.name) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}