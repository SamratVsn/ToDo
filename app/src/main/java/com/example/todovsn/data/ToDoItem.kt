package com.example.todovsn.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity(tableName = "Tasks")
data class ToDoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val category: String = "Extra",
    val dueDate: LocalDate? = null,
    val dueTime: LocalTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val isCompleted: Boolean = false
)