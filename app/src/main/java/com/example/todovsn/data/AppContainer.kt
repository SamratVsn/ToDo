package com.example.todovsn.data

import android.content.Context
import com.example.todovsn.data.preference.PreferenceRepository

interface AppContainer {
    val toDoRepository: ToDoRepository
    val userPreferencesRepository: PreferenceRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineToDoRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ToDoRepository]
     */
    override val toDoRepository: ToDoRepository by lazy {
        OfflineToDoRepository(ToDoDatabase.getDatabase(context).toDoDao())
    }

    override val userPreferencesRepository: PreferenceRepository by lazy {
        PreferenceRepository(context)
    }
}