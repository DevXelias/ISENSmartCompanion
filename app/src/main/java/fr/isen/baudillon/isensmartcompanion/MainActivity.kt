package fr.isen.baudillon.isensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
        BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.MainScreen,
        BottomNavItem.EventsScreen,
        BottomNavItem.AgendaScreen,
        BottomNavItem.HistoryScreen
    )
    NavigationBar {
        val currentDestination by navController.currentBackStackEntryAsState()
        val currentRoute = currentDestination?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.MainScreen.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.MainScreen.route) {
            MainScreen()
        }
        composable(BottomNavItem.EventsScreen.route) {
            EventsScreen()
        }
        composable(BottomNavItem.AgendaScreen.route) {
            AgendaScreen()
        }
        composable(BottomNavItem.HistoryScreen.route) {
            HistoryScreen()
        }

    }
    }



@Composable fun AgendaScreen() { Text("Écran Agenda") }
@Composable fun HistoryScreen() { Text("Écran Historique") }

sealed interface BottomNavItem {
    val route: String
    val label: String
    val icon: ImageVector

    data object MainScreen : BottomNavItem {
        override val route = "main"
        override val label = "Accueil"
        override val icon = Icons.Default.Home
    }

    data object EventsScreen : BottomNavItem {
        override val route = "events"
        override val label = "Événements"
        override val icon = Icons.Default.Place
    }

    data object AgendaScreen : BottomNavItem {
        override val route = "agenda"
        override val label = "Agenda"
        override val icon = Icons.Default.DateRange
    }

    data object HistoryScreen : BottomNavItem {
        override val route = "history"
        override val label = "Historique"
        override val icon = Icons.Default.Menu
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp()
}
