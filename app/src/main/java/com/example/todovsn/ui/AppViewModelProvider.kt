package com.example.todovsn.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todovsn.ToDoApplication
import com.example.todovsn.ui.home.HomeViewModel
import com.example.todovsn.ui.screens.AddViewModel
import com.example.todovsn.ui.screens.DetailsViewModel
import com.example.todovsn.ui.screens.EditViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            EditViewModel(
                this.createSavedStateHandle(),
                toDoApplication().container.toDoRepository
            )
        }

        initializer {
            DetailsViewModel(
                this.createSavedStateHandle(),
                toDoApplication().container.toDoRepository
            )
        }

        initializer {
            AddViewModel(toDoApplication().container.toDoRepository)
        }

        initializer {
            HomeViewModel(toDoApplication().container.toDoRepository)
        }
    }
}

fun CreationExtras.toDoApplication() : ToDoApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ToDoApplication)