package com.example.todovsn.data

data class ToDoItem(
    val id: Int,
    val title: String,
    val isCompleted: Boolean = false
)
