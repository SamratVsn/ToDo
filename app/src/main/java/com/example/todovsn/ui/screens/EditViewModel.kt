package com.example.todovsn.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todovsn.data.ToDoRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditViewModel(
    savedStateHandle: SavedStateHandle,
    private val toDoRepository: ToDoRepository
) : ViewModel() {
    var toDoUiState by mutableStateOf(ToDoUiState())
        private set

    private val toDoId: Int = checkNotNull(savedStateHandle[ToDoEditDestination.toDoIdArg])

    init{
        viewModelScope.launch {
            toDoUiState = toDoRepository.getToDoStream(toDoId)
                .filterNotNull()
                .first()
                .toToDoUiState(true)
        }
    }

    suspend fun updateToDo(){
        if(validateInput(toDoUiState.toDoDetails)){
            toDoRepository.updateToDo(toDoUiState.toDoDetails.toToDo())
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