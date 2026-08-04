package com.example.todovsn.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.todovsn.R
import com.example.todovsn.ui.navigation.NavDestination

object AddToDoDestination : NavDestination {
    override val route = "item_entry"
    override val titleRes = R.string.add_screen
}

@Composable
fun AddToDoScreen(
    onBackPressed : () -> Unit,
    modifier: Modifier = Modifier,
) {

}