package com.example.animaldiary.test.calendar

data class CalendarDay(
    val day: Int,
    val isCurrentMonth: Boolean,
    val isToday: Boolean = false
)