package com.example.todovsn.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(toDo: ToDoItem)

    @Update
    suspend fun update(toDo: ToDoItem)

    @Delete
    suspend fun delete(toDo: ToDoItem)

    @Query("SELECT * from items WHERE id = :id")
    fun getToDo(id: Int): Flow<ToDoItem?>

    @Query("SELECT * from items ORDER BY name ASC")
    fun getAllToDos(): Flow<List<ToDoItem>>
}