package com.example.todovsn.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todovsn.ToDoApplication
import com.example.todovsn.ui.home.HomeViewModel
import com.example.todovsn.ui.screens.AddViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            AddViewModel(ToDoApplication().container.toDoRepository)
        }

        initializer {
            HomeViewModel(ToDoApplication().container.toDoRepository)
        }
    }
}