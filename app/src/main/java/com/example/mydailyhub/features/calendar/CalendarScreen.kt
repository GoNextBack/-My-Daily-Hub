package com.example.mydailyhub.features.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    previousRoute: String,
    modifier: Modifier = Modifier,
) {
    val visibleMonth = viewModel.visibleMonth
    val today = viewModel.today
    val monthFormatter = remember {
        DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
    }
    val dayFormatter = remember {
        DateTimeFormatter.ofPattern("d")
    }
    val daysOfWeek = remember {
        DayOfWeek.values().toList()
    }
    val calendarDays = remember(visibleMonth) {
        val firstOfMonth = visibleMonth.atDay(1)
        val daysInMonth = visibleMonth.lengthOfMonth()
        val firstDayOffset = computeWeekOffset(firstOfMonth.dayOfWeek)
        val placeholders = List(firstDayOffset) { null }
        val actualDays = (1..daysInMonth).map { day -> visibleMonth.atDay(day) }
        placeholders + actualDays
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Calendar",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Previously viewed: ${previousRoute.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    IconButton(onClick = viewModel::goToPreviousMonth) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Previous month",
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = monthFormatter.format(visibleMonth),
                            modifier = Modifier.testTag("calendar_month_title"),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "Tap arrows to explore (placeholder)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    IconButton(onClick = viewModel::goToNextMonth) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Next month",
                        )
                    }
                }
                DayOfWeekHeader(daysOfWeek = daysOfWeek)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(calendarDays.size) { index ->
                        val date = calendarDays[index]
                        CalendarCell(
                            date = date,
                            today = today,
                            dayFormatter = dayFormatter,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DayOfWeekHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        for (day in daysOfWeek) {
            Text(
                text = day.name.take(3),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun CalendarCell(
    date: LocalDate?,
    today: LocalDate,
    dayFormatter: DateTimeFormatter,
    modifier: Modifier = Modifier,
) {
    val isToday = date == today
    val baseColor = when {
        date == null -> Color.Transparent
        isToday -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = when {
        date == null -> Color.Transparent
        isToday -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = baseColor,
                shape = MaterialTheme.shapes.small,
            )
            .height(36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (date == null) {
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Text(
                text = dayFormatter.format(date),
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor,
            )
        }
    }
}

private fun computeWeekOffset(dayOfWeek: DayOfWeek): Int {
    val firstDay = DayOfWeek.SUNDAY
    val raw = dayOfWeek.value - firstDay.value
    return (raw + DayOfWeek.entries.size) % DayOfWeek.entries.size
}
