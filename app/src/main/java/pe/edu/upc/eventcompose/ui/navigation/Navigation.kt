package pe.edu.upc.eventcompose.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pe.edu.upc.eventcompose.ui.calendar.Calendar
import pe.edu.upc.eventcompose.ui.calendar.CalendarViewModel
import pe.edu.upc.eventcompose.ui.events.Events
import pe.edu.upc.eventcompose.ui.events.EventsViewModel
import pe.edu.upc.eventcompose.ui.maps.Map
import pe.edu.upc.eventcompose.ui.maps.MapsViewModel

@Composable
fun Navigation() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }

    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(
                PaddingValues(
                    0.dp, 0.dp, 0.dp, innerPadding.calculateBottomPadding()
                )
            )
        ) {
            NavHost(
                navController = navController,
                startDestination = Routes.Map.route
            ) {
                composable(Routes.Map.route) {
                    val viewModel: MapsViewModel = hiltViewModel()
                    viewModel.fetchEventsByLocation()
                    Map(
                        viewModel
                    )
                }

                composable(Routes.Events.route) {
                    val viewModel: EventsViewModel = hiltViewModel()
                    //viewModel.fetchEvents()
                    Events(viewModel)
                }

                composable(Routes.Calendar.route) {
                    val viewModel: CalendarViewModel = hiltViewModel()
                    Calendar(viewModel)
                }
            }
        }
    }
}

sealed class Routes(val route: String, val vector: ImageVector, val title: String) {
    object Map : Routes("map", Icons.Filled.Map, "Map")
    object Events : Routes("events", Icons.Filled.CalendarViewMonth, "Events") {
        const val routeWithArgument = "events/{id}"
        const val argument = "id"
    }

    object Calendar : Routes("calendar", Icons.Filled.CalendarToday, "Calendar")
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Routes.Map,
        Routes.Events,
        Routes.Calendar
    )
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.onPrimary,
        contentColor = MaterialTheme.colors.onBackground
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        item.vector,
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title) },
                selectedContentColor = MaterialTheme.colors.onBackground,
                unselectedContentColor = MaterialTheme.colors.onBackground.copy(0.4f),
                alwaysShowLabel = false,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // re selecting the same item
                        launchSingleTop = true
                        // Restore state when re selecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}