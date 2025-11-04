package com.example.mydailyhub.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.ui.graphics.vector.ImageVector

sealed class HubDestination(
    val baseRoute: String,
    val title: String,
    val icon: ImageVector,
) {
    val routePattern: String = "$baseRoute?$ARG_FROM_ROUTE={$ARG_FROM_ROUTE}"

    fun buildRoute(fromRoute: String): String = "$baseRoute?$ARG_FROM_ROUTE=$fromRoute"

    data object Notes : HubDestination(
        baseRoute = "notes",
        title = "Notes",
        icon = Icons.Filled.Description,
    )

    data object Tasks : HubDestination(
        baseRoute = "tasks",
        title = "Tasks",
        icon = Icons.Filled.CheckCircle,
    )

    data object Calendar : HubDestination(
        baseRoute = "calendar",
        title = "Calendar",
        icon = Icons.Filled.CalendarToday,
    )

    companion object {
        const val ARG_FROM_ROUTE: String = "fromRoute"

        val destinations: List<HubDestination> = listOf(Notes, Tasks, Calendar)

        private val orderByRoute: Map<String, Int> =
            destinations.mapIndexed { index, destination -> destination.baseRoute to index }
                .toMap()

        fun fromRoute(route: String?): HubDestination = when (stripArguments(route)) {
            Notes.baseRoute -> Notes
            Tasks.baseRoute -> Tasks
            Calendar.baseRoute -> Calendar
            else -> Notes
        }

        fun stripArguments(route: String?): String =
            route?.substringBefore("?") ?: Notes.baseRoute

        fun transitionDirection(fromRoute: String?, toRoute: String): TransitionDirection {
            val fromIndex = orderByRoute[stripArguments(fromRoute)] ?: 0
            val toIndex = orderByRoute[stripArguments(toRoute)] ?: 0
            return when {
                toIndex > fromIndex -> TransitionDirection.Forward
                toIndex < fromIndex -> TransitionDirection.Backward
                else -> TransitionDirection.Neutral
            }
        }
    }
}

enum class TransitionDirection {
    Forward,
    Backward,
    Neutral,
}

fun TransitionDirection.reversed(): TransitionDirection = when (this) {
    TransitionDirection.Forward -> TransitionDirection.Backward
    TransitionDirection.Backward -> TransitionDirection.Forward
    TransitionDirection.Neutral -> TransitionDirection.Neutral
}
