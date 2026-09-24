package com.example.todovsn

import com.example.todovsn.ui.screens.ToDoDetails
import com.example.todovsn.ui.screens.isValidToDoEntry
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * QA: task title validation rule is title.isNotBlank() && trim().length >= 3.
 * Covers empty, whitespace, short, boundary, and valid inputs.
 */
class ToDoValidationTest {

    private fun details(title: String) = ToDoDetails(title = title, description = "desc")

    @Test
    fun emptyTitle_isInvalid() {
        assertFalse(isValidToDoEntry(details("")))
    }

    @Test
    fun blankTitle_isInvalid() {
        assertFalse(isValidToDoEntry(details("   ")))
    }

    @Test
    fun shortTitle_isInvalid() {
        assertFalse(isValidToDoEntry(details("ab")))
        assertFalse(isValidToDoEntry(details("  a  ")))
    }

    @Test
    fun boundaryThreeChars_isValid() {
        assertTrue(isValidToDoEntry(details("abc")))
        assertTrue(isValidToDoEntry(details("  abc  ")))
    }

    @Test
    fun normalTitle_isValid() {
        assertTrue(isValidToDoEntry(details("Study math")))
    }

    @Test
    fun whitespacePaddedShortTitle_isInvalid() {
        // " ab " trims to "ab" (length 2) -> invalid
        assertFalse(isValidToDoEntry(details(" ab ")))
    }
}
