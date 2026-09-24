package com.example.todovsn.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todovsn.data.ToDoItem
import com.example.todovsn.data.ToDoRepository
import com.example.todovsn.ui.screens.toToDo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val toDoRepository: ToDoRepository): ViewModel() {
    companion object {
        private const val TIMEOUT_MILLS = 5_000L
    }

    private var lastDeletedToDo: ToDoItem? = null

    val homeUiState : StateFlow<HomeUiState> = toDoRepository.getAllToDoStream().map { HomeUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLS),
            initialValue = HomeUiState()
        )

    fun deleteToDo(toDo: ToDoItem) {
        lastDeletedToDo = toDo
        viewModelScope.launch {
            toDoRepository.deleteToDo(toDo)
        }
    }

    fun restoreDeletedToDo() {
        val toDo = lastDeletedToDo ?: return
        viewModelScope.launch {
            toDoRepository.insertToDo(toDo)
            // Clear only after a successful restore so a failed insert
            // doesn't lose the undo buffer.
            if (lastDeletedToDo == toDo) {
                lastDeletedToDo = null
            }
        }
    }

    fun toggleCompleted(toDo: ToDoItem) {
        viewModelScope.launch {
            toDoRepository.updateToDo(
                toDo.copy(
                    isCompleted = !toDo.isCompleted
                )
            )
        }
    }
}

data class HomeUiState(
    val toDoList: List<ToDoItem> = listOf()
)