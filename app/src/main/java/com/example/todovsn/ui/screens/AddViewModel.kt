package com.example.todovsn.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todovsn.data.ToDoRepository
import com.example.todovsn.data.ToDoItem
import com.example.todovsn.data.preference.PreferenceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime

class AddViewModel(
    private val toDoRepository: ToDoRepository,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {
    var toDoUiState by mutableStateOf(ToDoUiState())
        private set

    val categories: StateFlow<List<String>> = toDoRepository.getAllCategoriesStream()
        .map { it.map { category -> category.name } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf("Study", "Work", "Productive", "Personal", "Important")
        )

    fun updateUiState(toDoDetails: ToDoDetails) {
        toDoUiState =
            ToDoUiState(toDoDetails = toDoDetails, isEntryValid = validateInput(toDoDetails))
    }

    suspend fun saveToDo() : Boolean{
        if (!validateInput()) return false

        toDoRepository.insertToDo(toDoUiState.toDoDetails.toToDo())
        preferenceRepository.incrementTotalTasksCreated()
        return true
    }

    private fun validateInput(uiState: ToDoDetails = toDoUiState.toDoDetails) : Boolean {
        return with(uiState) {
            title.isNotBlank() && title.trim().length >= 3
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
    val category: String = "Personal",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val isCompleted: Boolean = false,
)

fun ToDoDetails.toToDo(): ToDoItem = ToDoItem(
    id = id,
    title = title,
    description = description,
    category = category,
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
    createdAt = createdAt,
    isCompleted = isCompleted
)