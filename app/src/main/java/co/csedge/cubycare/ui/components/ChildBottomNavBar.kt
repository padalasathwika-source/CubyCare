package co.csedge.cubycare.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import co.csedge.cubycare.utils.tr

@Composable
fun ChildBottomNavBar(navController: NavController, currentRoute: String, childId: String) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val itemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )

        val childHomeRoute = "child_home/$childId"
        NavigationBarItem(
            selected = currentRoute == childHomeRoute,
            onClick = {
                if (currentRoute != childHomeRoute) {
                    navController.popBackStack("child_home/$childId", inclusive = false)
                }
            },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = itemColors
        )

        val growthRoute = "growth/$childId"
        NavigationBarItem(
            selected = currentRoute == growthRoute,
            onClick = {
                if (currentRoute != growthRoute) {
                    navController.navigate(growthRoute) {
                        popUpTo("child_home/$childId")
                        launchSingleTop = true
                    }
                }
            },
            icon = { Icon(Icons.Filled.Star, contentDescription = "Growth") },
            label = { Text("Growth") },
            colors = itemColors
        )

        val vaccinationRoute = "vaccination/$childId"
        NavigationBarItem(
            selected = currentRoute == vaccinationRoute,
            onClick = {
                if (currentRoute != vaccinationRoute) {
                    navController.navigate(vaccinationRoute) {
                        popUpTo("child_home/$childId")
                        launchSingleTop = true
                    }
                }
            },
            icon = { Icon(Icons.Filled.DateRange, contentDescription = "Vaccines") },
            label = { Text(tr("vaccines")) },
            colors = itemColors
        )

        val healthTrackerRoute = "health_tracker/$childId"
        NavigationBarItem(
            selected = currentRoute == healthTrackerRoute,
            onClick = {
                if (currentRoute != healthTrackerRoute) {
                    navController.navigate(healthTrackerRoute) {
                        popUpTo("child_home/$childId")
                        launchSingleTop = true
                    }
                }
            },
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Cuby Health") },
            label = { Text("Cuby Health") },
            colors = itemColors
        )
    }
}
