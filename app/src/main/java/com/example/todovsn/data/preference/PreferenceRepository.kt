package com.example.todovsn.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "todo_preferences")

class PreferenceRepository(private val context: Context) {
    val preferences: Flow<UserPreferences> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map{ prefs ->
            val name = prefs[Keys.DISPLAY_NAME] ?: "Guest"
            val theme = prefs[Keys.THEME_MODE]?.let {
                runCatching { ThemeMode.valueOf(it) }.getOrDefault(ThemeMode.SYSTEM)
            } ?: ThemeMode.SYSTEM
            val totalCreated = prefs[Keys.TOTAL_TASKS_CREATED] ?: 0
            val smartReminders = prefs[Keys.SMART_REMINDERS_ENABLED] ?: false
            UserPreferences(
                themeMode = theme, 
                displayName = name, 
                totalTasksCreated = totalCreated,
                smartRemindersEnabled = smartReminders
            )
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setDisplayName(name: String) {
        context.dataStore.edit { it[Keys.DISPLAY_NAME] = name.trim().ifBlank { "Guest" } }
    }

    suspend fun setSmartRemindersEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.SMART_REMINDERS_ENABLED] = enabled }
    }

    suspend fun incrementTotalTasksCreated() {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.TOTAL_TASKS_CREATED] ?: 0
            prefs[Keys.TOTAL_TASKS_CREATED] = current + 1
        }
    }

    suspend fun resetTotalTasksCreated() {
        context.dataStore.edit { it[Keys.TOTAL_TASKS_CREATED] = 0 }
    }

    suspend fun clearAllPreferences() {
        context.dataStore.edit { it.clear() }
    }

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DISPLAY_NAME = stringPreferencesKey("display_name")
        val TOTAL_TASKS_CREATED = intPreferencesKey("total_tasks_created")
        val SMART_REMINDERS_ENABLED = booleanPreferencesKey("smart_reminders_enabled")
    }
}
