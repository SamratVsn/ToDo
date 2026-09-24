package com.example.todovsn.data.preference

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val displayName: String = "Guest",
    val totalTasksCreated: Int = 0,
    val smartRemindersEnabled: Boolean = false,
    val focusSessionsToday: Int = 0,
    val lastFocusDate: String = "" // ISO-8601 date string
)

fun resolveSessionsToday(lastDate: String, storedCount: Int, today: String): Int {
    return if (lastDate == today) storedCount else 0
}

fun normalizeDisplayName(raw: String): String = raw.trim().ifBlank { "Guest" }
