package com.example.todovsn

import com.example.todovsn.data.DateConverters
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * QA: Room type converters — null-safety, ISO round-trips, and corrupt-data
 * tolerance (corrupt strings must yield null, not crash the Flow).
 */
class DateConvertersTest {

    private val converters = DateConverters()

    @Test
    fun localDateTime_roundTrip() {
        val value = LocalDateTime.of(2026, 9, 23, 14, 45, 12)
        assertEquals(value, converters.toLocalDateTime(converters.fromLocalDateTime(value)))
    }

    @Test
    fun localDate_roundTrip() {
        val value = LocalDate.of(2026, 9, 23)
        assertEquals(value, converters.toLocalDate(converters.fromLocalDate(value)))
    }

    @Test
    fun localTime_roundTrip() {
        val value = LocalTime.of(9, 30)
        assertEquals(value, converters.toLocalTime(converters.fromLocalTime(value)))
    }

    @Test
    fun nullStaysNull() {
        assertNull(converters.fromLocalDateTime(null))
        assertNull(converters.toLocalDateTime(null))
        assertNull(converters.fromLocalDate(null))
        assertNull(converters.toLocalDate(null))
        assertNull(converters.fromLocalTime(null))
        assertNull(converters.toLocalTime(null))
    }

    @Test
    fun corruptString_returnsNullInsteadOfThrowing() {
        assertNull(converters.toLocalDateTime("not-a-date"))
        assertNull(converters.toLocalDate("not-a-date"))
        assertNull(converters.toLocalTime("not-a-date"))
    }
}
