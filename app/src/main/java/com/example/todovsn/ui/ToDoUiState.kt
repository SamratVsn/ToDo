package com.example.todovsn.ui

import com.example.todovsn.data.ToDoItem

data class TodoUiState(
    val items: List<ToDoItem> = emptyList()
)