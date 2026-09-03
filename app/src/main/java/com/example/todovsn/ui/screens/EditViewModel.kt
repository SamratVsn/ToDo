package com.example.todovsn.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todovsn.data.ToDoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditViewModel(
    savedStateHandle: SavedStateHandle,
    private val toDoRepository: ToDoRepository
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

    private val toDoId: Int = checkNotNull(savedStateHandle[ToDoEditDestination.toDoIdArg])

    init{
        viewModelScope.launch {
            toDoUiState = toDoRepository.getToDoStream(toDoId)
                .filterNotNull()
                .first()
                .toToDoUiState(true)
        }
    }

    suspend fun updateToDo(): Boolean {
        return if (validateInput(toDoUiState.toDoDetails)) {
            toDoRepository.updateToDo(toDoUiState.toDoDetails.toToDo())
            true
        } else {
            false
        }
    }

    fun updateUiState(toDoDetails: ToDoDetails){
        toDoUiState =
            ToDoUiState(toDoDetails = toDoDetails, isEntryValid = validateInput(toDoDetails))
    }

    private fun validateInput(uiState: ToDoDetails = toDoUiState.toDoDetails) : Boolean {
            return with(uiState){
                title.isNotBlank() && title.trim().length >= 3
            }
    }
}
