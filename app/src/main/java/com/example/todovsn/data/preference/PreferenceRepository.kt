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
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.LocalDate

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "todo_preferences")

class PreferenceRepository(private val context: Context) {
    // IOException (e.g. corrupt prefs file) -> fall back to defaults.
    // Other exceptions propagate. Equivalent to the documented
    // dataStore.data.catch { } pattern, but avoids the deprecated
    // SharedFlow.catch overload resolution warning.
    val preferences: Flow<UserPreferences> = flow {
        try {
            emitAll(context.dataStore.data)
        } catch (exception: IOException) {
            emit(emptyPreferences())
        }
    }
        .map { prefs ->
            val name = prefs[Keys.DISPLAY_NAME] ?: "Guest"
            val theme = prefs[Keys.THEME_MODE]?.let {
                runCatching { ThemeMode.valueOf(it) }.getOrDefault(ThemeMode.SYSTEM)
            } ?: ThemeMode.SYSTEM
            val totalCreated = prefs[Keys.TOTAL_TASKS_CREATED] ?: 0
            val smartReminders = prefs[Keys.SMART_REMINDERS_ENABLED] ?: false
            
            val lastDate = prefs[Keys.LAST_FOCUS_DATE] ?: ""
            val today = LocalDate.now().toString()
            
            val sessionsToday = resolveSessionsToday(
                lastDate = lastDate,
                storedCount = prefs[Keys.FOCUS_SESSIONS_TODAY] ?: 0,
                today = today
            )

            UserPreferences(
                themeMode = theme,
                displayName = name,
                totalTasksCreated = totalCreated,
                smartRemindersEnabled = smartReminders,
                focusSessionsToday = sessionsToday,
                lastFocusDate = lastDate
            )
        }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    suspend fun setDisplayName(name: String) {
        context.dataStore.edit { it[Keys.DISPLAY_NAME] = normalizeDisplayName(name) }
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

    suspend fun incrementFocusSessions() {
        val today = LocalDate.now().toString()
        context.dataStore.edit { prefs ->
            val lastDate = prefs[Keys.LAST_FOCUS_DATE] ?: ""
            val current = if (lastDate == today) {
                prefs[Keys.FOCUS_SESSIONS_TODAY] ?: 0
            } else {
                0
            }
            prefs[Keys.FOCUS_SESSIONS_TODAY] = current + 1
            prefs[Keys.LAST_FOCUS_DATE] = today
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
        val FOCUS_SESSIONS_TODAY = intPreferencesKey("focus_sessions_today")
        val LAST_FOCUS_DATE = stringPreferencesKey("last_focus_date")
    }
}
