package com.example.todovsn.ui

import com.example.todovsn.model.ToDoItem

data class TodoUiState(
    val items: List<ToDoItem> = emptyList()
)