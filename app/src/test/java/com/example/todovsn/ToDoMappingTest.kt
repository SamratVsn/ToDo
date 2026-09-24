package com.example.todovsn

import com.example.todovsn.data.ToDoItem
import com.example.todovsn.ui.screens.ToDoDetails
import com.example.todovsn.ui.screens.toToDo
import com.example.todovsn.ui.screens.toToDoDetails
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

/**
 * QA: bidirectional mapping between persistence ([ToDoItem]) and UI ([ToDoDetails])
 * must preserve every field.
 */
class ToDoMappingTest {

    @Test
    fun toToDo_preservesAllFields() {
        val created = LocalDateTime.of(2026, 9, 1, 10, 30)
        val details = ToDoDetails(
            id = 7,
            title = "Write report",
            description = "Q3 summary",
            category = "Work",
            createdAt = created,
            isCompleted = true
        )

        val item = details.toToDo()

        assertEquals(7, item.id)
        assertEquals("Write report", item.title)
        assertEquals("Q3 summary", item.description)
        assertEquals("Work", item.category)
        assertEquals(created, item.createdAt)
        assertEquals(true, item.isCompleted)
    }

    @Test
    fun toToDoDetails_preservesAllFields() {
        val created = LocalDateTime.of(2026, 5, 20, 8, 0)
        val item = ToDoItem(
            id = 3,
            title = "Gym",
            description = "Leg day",
            category = "Personal",
            createdAt = created,
            isCompleted = false
        )

        val details = item.toToDoDetails()

        assertEquals(3, details.id)
        assertEquals("Gym", details.title)
        assertEquals("Leg day", details.description)
        assertEquals("Personal", details.category)
        assertEquals(created, details.createdAt)
        assertEquals(false, details.isCompleted)
    }

    @Test
    fun roundTrip_isIdentity() {
        val original = ToDoDetails(title = "Read", description = "Ch. 4", category = "Study")
        assertEquals(original, original.toToDo().toToDoDetails())
    }
}
