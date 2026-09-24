package com.example.todovsn

import com.example.todovsn.ui.screens.FocusUiState
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * QA: focus timer derived UI state (MM:SS formatting + progress fraction).
 */
class FocusUiStateTest {

    @Test
    fun formattedTime_zero() {
        assertEquals("00:00", FocusUiState(timeLeftSeconds = 0).formattedTime)
    }

    @Test
    fun formattedTime_padsSingleDigits() {
        assertEquals("01:05", FocusUiState(timeLeftSeconds = 65).formattedTime)
    }

    @Test
    fun formattedTime_standardPomodoro() {
        assertEquals("25:00", FocusUiState(timeLeftSeconds = 1500).formattedTime)
    }

    @Test
    fun formattedTime_hourPlus() {
        // 90 min -> 90:00 (no hour rollover by design)
        assertEquals("90:00", FocusUiState(timeLeftSeconds = 5400).formattedTime)
    }

    @Test
    fun progress_full() {
        assertEquals(1f, FocusUiState(totalTimeSeconds = 1500, timeLeftSeconds = 1500).progress)
    }

    @Test
    fun progress_half() {
        assertEquals(0.5f, FocusUiState(totalTimeSeconds = 1500, timeLeftSeconds = 750).progress)
    }

    @Test
    fun progress_empty() {
        assertEquals(0f, FocusUiState(totalTimeSeconds = 1500, timeLeftSeconds = 0).progress)
    }

    @Test
    fun progress_zeroTotal_returnsZero_notNaN() {
        // Guards against division-by-zero producing NaN in the progress bar.
        assertEquals(0f, FocusUiState(totalTimeSeconds = 0, timeLeftSeconds = 0).progress)
    }
}
