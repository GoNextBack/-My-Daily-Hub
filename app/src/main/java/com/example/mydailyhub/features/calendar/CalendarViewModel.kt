package com.example.mydailyhub.features.calendar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.YearMonth

class CalendarViewModel : ViewModel() {

    val today: LocalDate = LocalDate.now()

    var visibleMonth by mutableStateOf(YearMonth.from(today))
        private set

    fun goToPreviousMonth() {
        visibleMonth = visibleMonth.minusMonths(1)
    }

    fun goToNextMonth() {
        visibleMonth = visibleMonth.plusMonths(1)
    }
}
