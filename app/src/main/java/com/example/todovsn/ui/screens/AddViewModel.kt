package com.example.todovsn.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.todovsn.data.ToDoRepository
import com.example.todovsn.data.ToDoItem
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class AddViewModel(private val toDoRepository: ToDoRepository) : ViewModel() {
    var toDoUiState by mutableStateOf(ToDoUiState())
        private set

    fun updateUiState(toDoDetails: ToDoDetails) {
        toDoUiState =
            ToDoUiState(toDoDetails = toDoDetails, isEntryValid = validateInput(toDoDetails))
    }

    suspend fun saveToDo() : Boolean{
        if (!validateInput()) return false

        toDoRepository.insertToDo(toDoUiState.toDoDetails.toToDo())
        return true
    }

    private fun validateInput(uiState: ToDoDetails = toDoUiState.toDoDetails) : Boolean {
        return with(uiState) {
            title.isNotBlank() && title.trim().length >= 3 && dueDate != null
        }
    }
}

data class ToDoUiState(
    val toDoDetails: ToDoDetails = ToDoDetails(),
    val isEntryValid: Boolean = false
)

data class ToDoDetails(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val category: String = "Extra",
    val dueDate: LocalDate? = null,
    val dueTime: LocalTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val isCompleted: Boolean = false,
)

fun ToDoDetails.toToDo(): ToDoItem = ToDoItem(
    id = id,
    title = title,
    description = description,
    category = category,
    dueDate = dueDate,
    dueTime = dueTime,
    createdAt = createdAt,
    isCompleted = isCompleted
)

fun ToDoItem.toToDoUiState(isEntryValid: Boolean = false) : ToDoUiState = ToDoUiState(
    toDoDetails = this.toToDoDetails(),
    isEntryValid = isEntryValid
)

fun ToDoItem.toToDoDetails() : ToDoDetails = ToDoDetails(
    id = id,
    title = title,
    description = description,
    category = category,
    dueDate = dueDate,
    dueTime = dueTime,
    createdAt = createdAt,
    isCompleted = isCompleted
)