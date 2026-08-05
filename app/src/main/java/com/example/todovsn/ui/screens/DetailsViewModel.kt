package com.example.todovsn.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todovsn.data.ToDoRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val toDoRepository: ToDoRepository
) : ViewModel() {
    private val toDoId: Int = checkNotNull(savedStateHandle[ToDoDetailsDestination.toDoIdArg])

    val uiState: StateFlow<ToDoDetailsUiState> =
        toDoRepository.getToDoStream(toDoId)
            .filterNotNull()
            .map {
                ToDoDetailsUiState(
                    toDoDetails = it.toToDoDetails()
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ToDoDetailsUiState()
            )

    fun deleteToDo() {
        viewModelScope.launch {
            toDoRepository.deleteToDo(
                uiState.value.toDoDetails.toToDo()
            )
        }
    }

    fun toggleCompleted() {
        viewModelScope.launch {
            val currentTask = uiState.value.toDoDetails.toToDo()
            toDoRepository.updateToDo(
                currentTask.copy(
                    isCompleted = !currentTask.isCompleted
                )
            )
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class ToDoDetailsUiState(
    val toDoDetails: ToDoDetails = ToDoDetails()
)