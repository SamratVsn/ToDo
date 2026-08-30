package com.example.todovsn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.todovsn.data.ToDoRepository
import com.example.todovsn.data.preference.PreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class ProfileUiState(
    val displayName: String = "Guest",
    val isEditDialogOpen: Boolean = false,
    val totalTasks: Int = 0,
    val tasksDone: Int = 0,
    val tasksToday: Int = 0,
    val currentTheme: String = "System"
)

@RequiresApi(Build.VERSION_CODES.O)
class ProfileViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val toDoRepository: ToDoRepository
) : ViewModel() {
    private val _isEditDialogOpen = MutableStateFlow(false)

    val uiState: StateFlow<ProfileUiState> = combine(
        preferenceRepository.preferences,
        toDoRepository.getAllToDoStream(),
        _isEditDialogOpen
    ) { prefs, tasks, isDialogOpen ->
        val today = LocalDate.now()
        ProfileUiState(
            displayName = prefs.displayName.ifBlank { "Guest" },
            isEditDialogOpen = isDialogOpen,
            totalTasks = prefs.totalTasksCreated,
            tasksDone = tasks.count { it.isCompleted },
            tasksToday = tasks.count { it.createdAt.toLocalDate() == today },
            currentTheme = prefs.themeMode.name.lowercase().replaceFirstChar { it.uppercase() }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState()
    )

    fun onEditNameClick() {
        _isEditDialogOpen.value = true
    }

    fun onDismissEditDialog() {
        _isEditDialogOpen.value = false
    }

    fun updateDisplayName(name: String) {
        val cleaned = name.trim()
        viewModelScope.launch {
            preferenceRepository.setDisplayName(cleaned)
        }
        _isEditDialogOpen.value = false
    }
}