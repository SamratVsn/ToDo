package com.example.todovsn.data

import kotlinx.coroutines.flow.Flow

class OfflineToDoRepository(private val toDoDao: ToDoDao) : ToDoRepository {
    override fun getAllToDoStream(): Flow<List<ToDoItem>> = toDoDao.getAllToDos()

    override fun getToDoStream(id: Int): Flow<ToDoItem?> = toDoDao.getToDo(id)

    override suspend fun insertToDo(item: ToDoItem) = toDoDao.insert(item)

    override suspend fun deleteToDo(item: ToDoItem) = toDoDao.delete(item)

    override suspend fun updateToDo(item: ToDoItem) = toDoDao.update(item)
}