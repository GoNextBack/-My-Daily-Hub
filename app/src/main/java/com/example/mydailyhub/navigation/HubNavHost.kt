package com.example.mydailyhub.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mydailyhub.features.calendar.CalendarScreen
import com.example.mydailyhub.features.calendar.CalendarViewModel
import com.example.mydailyhub.features.notes.NotesScreen
import com.example.mydailyhub.features.notes.NotesViewModel
import com.example.mydailyhub.features.tasks.TasksScreen
import com.example.mydailyhub.features.tasks.TasksViewModel

private const val TRANSITION_DURATION = 300

@Composable
fun HubNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HubDestination.Notes.routePattern,
        modifier = modifier,
    ) {
        composable(
            route = HubDestination.Notes.routePattern,
            arguments = baseArguments(),
            enterTransition = { enterTransitionFor(HubDestination.Notes.baseRoute) },
            exitTransition = { exitTransitionFor() },
            popEnterTransition = { popEnterTransitionFor(HubDestination.Notes.baseRoute) },
            popExitTransition = { popExitTransitionFor() },
        ) { backStackEntry ->
            val viewModel: NotesViewModel = viewModel(backStackEntry)
            NotesScreen(
                viewModel = viewModel,
                previousRoute = HubDestination.stripArguments(
                    backStackEntry.arguments?.getString(HubDestination.ARG_FROM_ROUTE),
                ),
            )
        }
        composable(
            route = HubDestination.Tasks.routePattern,
            arguments = baseArguments(),
            enterTransition = { enterTransitionFor(HubDestination.Tasks.baseRoute) },
            exitTransition = { exitTransitionFor() },
            popEnterTransition = { popEnterTransitionFor(HubDestination.Tasks.baseRoute) },
            popExitTransition = { popExitTransitionFor() },
        ) { backStackEntry ->
            val viewModel: TasksViewModel = viewModel(backStackEntry)
            TasksScreen(
                viewModel = viewModel,
                previousRoute = HubDestination.stripArguments(
                    backStackEntry.arguments?.getString(HubDestination.ARG_FROM_ROUTE),
                ),
            )
        }
        composable(
            route = HubDestination.Calendar.routePattern,
            arguments = baseArguments(),
            enterTransition = { enterTransitionFor(HubDestination.Calendar.baseRoute) },
            exitTransition = { exitTransitionFor() },
            popEnterTransition = { popEnterTransitionFor(HubDestination.Calendar.baseRoute) },
            popExitTransition = { popExitTransitionFor() },
        ) { backStackEntry ->
            val viewModel: CalendarViewModel = viewModel(backStackEntry)
            CalendarScreen(
                viewModel = viewModel,
                previousRoute = HubDestination.stripArguments(
                    backStackEntry.arguments?.getString(HubDestination.ARG_FROM_ROUTE),
                ),
            )
        }
    }
}

private fun baseArguments() = listOf(
    navArgument(HubDestination.ARG_FROM_ROUTE) {
        type = NavType.StringType
        defaultValue = HubDestination.Notes.baseRoute
    },
)

private fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransitionFor(
    targetRoute: String,
): EnterTransition {
    val direction = HubDestination.transitionDirection(
        fromRoute = targetState.arguments?.getString(HubDestination.ARG_FROM_ROUTE),
        toRoute = targetRoute,
    )
    return direction.toEnterTransition()
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransitionFor(): ExitTransition {
    val incomingRoute = HubDestination.stripArguments(targetState.destination.route)
    val direction = HubDestination.transitionDirection(
        fromRoute = targetState.arguments?.getString(HubDestination.ARG_FROM_ROUTE),
        toRoute = incomingRoute,
    )
    return direction.toExitTransition()
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransitionFor(
    targetRoute: String,
): EnterTransition {
    val originRoute = HubDestination.stripArguments(initialState.destination.route)
    val direction = HubDestination.transitionDirection(
        fromRoute = originRoute,
        toRoute = targetRoute,
    ).reversed()
    return direction.toEnterTransition()
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransitionFor(): ExitTransition {
    val destinationRoute = HubDestination.stripArguments(targetState.destination.route)
    val originRoute = HubDestination.stripArguments(initialState.destination.route)
    val direction = HubDestination.transitionDirection(
        fromRoute = originRoute,
        toRoute = destinationRoute,
    ).reversed()
    return direction.toExitTransition()
}

private fun TransitionDirection.toEnterTransition(): EnterTransition = when (this) {
    TransitionDirection.Forward ->
        slideInHorizontally(
            animationSpec = tween(TRANSITION_DURATION),
            initialOffsetX = { width -> width },
        ) + fadeIn(animationSpec = tween(TRANSITION_DURATION))

    TransitionDirection.Backward ->
        slideInHorizontally(
            animationSpec = tween(TRANSITION_DURATION),
            initialOffsetX = { width -> -width },
        ) + fadeIn(animationSpec = tween(TRANSITION_DURATION))

    TransitionDirection.Neutral ->
        fadeIn(animationSpec = tween(TRANSITION_DURATION))
}

private fun TransitionDirection.toExitTransition(): ExitTransition = when (this) {
    TransitionDirection.Forward ->
        slideOutHorizontally(
            animationSpec = tween(TRANSITION_DURATION),
            targetOffsetX = { width -> -width },
        ) + fadeOut(animationSpec = tween(TRANSITION_DURATION))

    TransitionDirection.Backward ->
        slideOutHorizontally(
            animationSpec = tween(TRANSITION_DURATION),
            targetOffsetX = { width -> width },
        ) + fadeOut(animationSpec = tween(TRANSITION_DURATION))

    TransitionDirection.Neutral ->
        fadeOut(animationSpec = tween(TRANSITION_DURATION))
}
