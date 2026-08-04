package com.example.todovsn.data

import kotlinx.coroutines.flow.Flow

interface ToDoRepository {
    fun getAllToDoStream() : Flow<List<ToDoItem>>
    fun getToDoStream(id: Int) : Flow<ToDoItem?>
    suspend fun insertToDo(item: ToDoItem)

    suspend fun deleteToDo(item: ToDoItem)

    suspend fun updateToDo(item: ToDoItem)
}