package com.example.todovsn.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "Tasks")
data class ToDoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val category: String = "Personal",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val isCompleted: Boolean = false
)