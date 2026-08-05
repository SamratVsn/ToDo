package com.example.todovsn.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todovsn.ToDoApplication
import com.example.todovsn.ui.home.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(ToDoApplication().container.toDoRepository)
        }
    }
}