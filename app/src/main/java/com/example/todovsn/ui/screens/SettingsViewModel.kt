package com.example.todovsn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todovsn.data.Category
import com.example.todovsn.data.ToDoRepository
import com.example.todovsn.data.preference.PreferenceRepository
import com.example.todovsn.data.preference.ThemeMode
import com.example.todovsn.data.preference.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val preferences: UserPreferences = UserPreferences(),
    val categories: List<Category> = listOf(),
    val isNameDialogOpen: Boolean = false,
    val isThemeDialogOpen: Boolean = false,
    val isDeleteConfirmationOpen: Boolean = false,
    val isResetConfirmationOpen: Boolean = false,
    val isCategoryManagementOpen: Boolean = false
)

class SettingsViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val toDoRepository: ToDoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    
    val uiState: StateFlow<SettingsUiState> = combine(
        preferenceRepository.preferences,
        toDoRepository.getAllCategoriesStream(),
        _uiState
    ) { prefs, categories, state ->
        state.copy(preferences = prefs, categories = categories)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            preferenceRepository.setThemeMode(mode)
        }
        _uiState.value = _uiState.value.copy(isThemeDialogOpen = false)
    }

    fun setSmartRemindersEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferenceRepository.setSmartRemindersEnabled(enabled)
        }
    }

    fun updateDisplayName(name: String) {
        viewModelScope.launch {
            preferenceRepository.setDisplayName(name)
        }
        _uiState.value = _uiState.value.copy(isNameDialogOpen = false)
    }

    fun resetTotalTasks() {
        viewModelScope.launch {
            preferenceRepository.resetTotalTasksCreated()
        }
        _uiState.value = _uiState.value.copy(isResetConfirmationOpen = false)
    }

    fun deleteAllData() {
        viewModelScope.launch {
            toDoRepository.deleteAllToDo()
            preferenceRepository.clearAllPreferences()
        }
        _uiState.value = _uiState.value.copy(isDeleteConfirmationOpen = false)
    }

    fun addCategory(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            toDoRepository.insertCategory(Category(name))
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            toDoRepository.deleteCategoryAndMoveTasks(category)
        }
    }

    fun showNameDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(isNameDialogOpen = show)
    }

    fun showThemeDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(isThemeDialogOpen = show)
    }

    fun showDeleteConfirmation(show: Boolean) {
        _uiState.value = _uiState.value.copy(isDeleteConfirmationOpen = show)
    }

    fun showResetConfirmation(show: Boolean) {
        _uiState.value = _uiState.value.copy(isResetConfirmationOpen = show)
    }

    fun showCategoryManagement(show: Boolean) {
        _uiState.value = _uiState.value.copy(isCategoryManagementOpen = show)
    }
}
