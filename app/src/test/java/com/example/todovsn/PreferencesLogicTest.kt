package com.example.todovsn

import com.example.todovsn.data.preference.normalizeDisplayName
import com.example.todovsn.data.preference.resolveSessionsToday
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * QA: DataStore-adjacent pure rules — daily focus-session rollover and
 * display-name normalization.
 */
class PreferencesLogicTest {

    @Test
    fun sessionsToday_sameDay_returnsStored() {
        assertEquals(3, resolveSessionsToday("2026-09-23", 3, "2026-09-23"))
    }

    @Test
    fun sessionsToday_newDay_resetsToZero() {
        assertEquals(0, resolveSessionsToday("2026-09-22", 5, "2026-09-23"))
    }

    @Test
    fun sessionsToday_neverTracked_resetsToZero() {
        assertEquals(0, resolveSessionsToday("", 4, "2026-09-23"))
    }

    @Test
    fun normalizeDisplayName_trims() {
        assertEquals("Sam", normalizeDisplayName("  Sam  "))
    }

    @Test
    fun normalizeDisplayName_blankBecomesGuest() {
        assertEquals("Guest", normalizeDisplayName(""))
        assertEquals("Guest", normalizeDisplayName("   "))
    }
}
