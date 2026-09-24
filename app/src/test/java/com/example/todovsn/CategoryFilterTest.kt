package com.example.todovsn

import com.example.todovsn.data.ToDoItem
import com.example.todovsn.ui.screens.filterCategoryTasks
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

/**
 * QA: category screen search + category filtering.
 */
class CategoryFilterTest {

    private val now = LocalDateTime.of(2026, 9, 23, 12, 0)
    private val tasks = listOf(
        ToDoItem(id = 1, title = "Study math", description = "Algebra", category = "Study", createdAt = now),
        ToDoItem(id = 2, title = "Gym", description = "Leg day workout", category = "Personal", createdAt = now),
        ToDoItem(id = 3, title = "Work report", description = "Q3 MATH summary", category = "Work", createdAt = now)
    )

    @Test
    fun emptyQuery_allCategory_returnsAll() {
        assertEquals(3, filterCategoryTasks(tasks, "", "All").size)
    }

    @Test
    fun query_matchesTitleCaseInsensitive() {
        val result = filterCategoryTasks(tasks, "gym", "All")
        assertEquals(listOf(2), result.map { it.id })
    }

    @Test
    fun query_matchesDescriptionCaseInsensitive() {
        // "MATH" appears in task 1 title and task 3 description
        val result = filterCategoryTasks(tasks, "math", "All")
        assertEquals(setOf(1, 3), result.map { it.id }.toSet())
    }

    @Test
    fun categoryFilter_exactMatch() {
        val result = filterCategoryTasks(tasks, "", "Study")
        assertEquals(listOf(1), result.map { it.id })
    }

    @Test
    fun queryAndCategory_combined() {
        // "math" + Study -> only task 1; "math" + Work -> only task 3
        assertEquals(listOf(1), filterCategoryTasks(tasks, "math", "Study").map { it.id })
        assertEquals(listOf(3), filterCategoryTasks(tasks, "math", "Work").map { it.id })
    }

    @Test
    fun noMatch_returnsEmpty() {
        assertEquals(0, filterCategoryTasks(tasks, "zzz-no-match", "All").size)
        assertEquals(0, filterCategoryTasks(tasks, "", "NonExistent").size)
    }

    @Test
    fun emptyList_returnsEmpty() {
        assertEquals(0, filterCategoryTasks(emptyList(), "math", "All").size)
    }
}
