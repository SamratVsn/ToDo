package com.example.todovsn.model

data class ToDoItem(
    val id: Int,
    val title: String,
    val isCompleted: Boolean = false
)
