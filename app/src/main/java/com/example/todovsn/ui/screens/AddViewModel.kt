package com.example.todovsn.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todovsn.data.ToDoRepository
import com.example.todovsn.data.ToDoItem
import kotlinx.coroutines.launch

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
            title.isNotBlank() && description.isNotBlank() && title.trim().length >= 3 && description.length <= 500
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
    val isCompleted: Boolean = false,
)

fun ToDoDetails.toToDo(): ToDoItem = ToDoItem(
    id = id,
    title = title,
    description = description,
)

fun ToDoItem.toToDoUiState(isEntryValid: Boolean = false) : ToDoUiState = ToDoUiState(
    toDoDetails = this.toToDoDetails(),
    isEntryValid = isEntryValid
)

fun ToDoItem.toToDoDetails() : ToDoDetails = ToDoDetails(
    id = id,
    title = title,
    description = description
)