package com.example.todovsn

import android.app.Application
import com.example.todovsn.data.AppContainer
import com.example.todovsn.data.AppDataContainer
import com.example.todovsn.data.Category
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ToDoApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        MainScope().launch {
            val defaults = listOf("Study", "Work", "Productive", "Personal", "Important")
            defaults.forEach { name ->
                container.toDoRepository.insertCategory(Category(name))
            }
        }
    }
}
