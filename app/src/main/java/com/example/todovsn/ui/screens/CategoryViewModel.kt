package com.example.todovsn.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todovsn.data.Category
import com.example.todovsn.data.ToDoItem
import com.example.todovsn.data.ToDoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CategoryUiState(
    val tasks: List<ToDoItem> = listOf(),
    val categories: List<String> = listOf("All"),
    val selectedCategory: String = "All",
    val searchQuery: String = ""
)

class CategoryViewModel(private val toDoRepository: ToDoRepository) : ViewModel() {

    private val _selectedCategory = MutableStateFlow("All")
    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<CategoryUiState> = combine(
        toDoRepository.getAllToDoStream(),
        toDoRepository.getAllCategoriesStream(),
        _selectedCategory,
        _searchQuery
    ) { tasks, dbCategories, selected, query ->
        val allCategories = listOf("All") + dbCategories.map { it.name }
        
        val filteredTasks = tasks.filter { task ->
            val matchesQuery = task.title.contains(query, ignoreCase = true) ||
                    task.description.contains(query, ignoreCase = true)
            val matchesCategory = if (selected == "All") true else task.category == selected
            matchesQuery && matchesCategory
        }

        CategoryUiState(
            tasks = filteredTasks,
            categories = allCategories,
            selectedCategory = selected,
            searchQuery = query
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CategoryUiState()
    )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }
}