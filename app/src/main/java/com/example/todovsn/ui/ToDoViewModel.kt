package com.example.todovsn.ui

import androidx.lifecycle.ViewModel
import com.example.todovsn.model.ToDoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ToDoViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState : StateFlow<TodoUiState> = _uiState.asStateFlow()

    private var nextId = 10801

    fun addToDo(title: String){
        if(title.isBlank()) return

        val newItem = ToDoItem(id = nextId++, title = title)
        _uiState.update { currentState ->
            currentState.copy(items = currentState.items + newItem)
        }
    }

    fun deleteToDo(id: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                items = currentState.items.filter { it.id != id }
            )
        }
    }

    fun checkToDo(id: Int){
        _uiState.update { currentState ->
            val updatedList = currentState.items.map { item ->
                if(item.id == id) item.copy(isCompleted = !item.isCompleted) else item
            }
            currentState.copy(items = updatedList)
        }
    }
}