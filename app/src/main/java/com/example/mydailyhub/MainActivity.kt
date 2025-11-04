package com.example.mydailyhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mydailyhub.navigation.HubDestination
import com.example.mydailyhub.navigation.HubNavHost
import com.example.mydailyhub.ui.theme.MyDailyHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyDailyHubTheme {
                MyDailyHubApp()
            }
        }
    }
}

@Composable
fun MyDailyHubApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val activeDestination = HubDestination.fromRoute(navBackStackEntry?.destination?.route)
    val currentRoute = activeDestination.baseRoute

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            HubBottomNavigation(
                destinations = HubDestination.destinations,
                currentRoute = currentRoute,
            ) { destination ->
                if (destination.baseRoute == currentRoute) return@HubBottomNavigation
                val previousRoute = activeDestination.baseRoute
                navController.navigate(destination.buildRoute(previousRoute)) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        },
    ) { innerPadding ->
        HubNavHost(
            navController = navController,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        )
    }
}

@Composable
private fun HubBottomNavigation(
    destinations: List<HubDestination>,
    currentRoute: String,
    onDestinationSelected: (HubDestination) -> Unit,
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        destinations.forEach { destination ->
            val isSelected = destination.baseRoute == currentRoute
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.title,
                    )
                },
                label = { Text(destination.title) },
                selected = isSelected,
                onClick = { onDestinationSelected(destination) },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyDailyHubPreview() {
    MyDailyHubTheme {
        MyDailyHubApp()
    }
}
