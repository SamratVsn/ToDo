package com.example.todovsn.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "Tasks")
data class ToDoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1080,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false
)
