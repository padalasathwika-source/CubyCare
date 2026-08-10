package co.csedge.cubycare.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.csedge.cubycare.utils.AppLanguageManager

@Composable
fun CubyBottomNavBar(navController: NavController, currentRoute: String) {
    val context = LocalContext.current
    val currentLang = AppLanguageManager.getSavedLanguage(context)

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

        NavigationBarItem(
            selected = currentRoute == "dashboard",
            onClick = {
                if (currentRoute != "dashboard") {
                    navController.navigate("dashboard") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = { Icon(Icons.Filled.Face, contentDescription = AppLanguageManager.getString("nav_kids", currentLang)) },
            label = { Text(AppLanguageManager.getString("nav_kids", currentLang)) },
            colors = itemColors
        )
        
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = {
                if (currentRoute != "profile") {
                    navController.navigate("profile") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = { Icon(Icons.Filled.Person, contentDescription = AppLanguageManager.getString("nav_parent", currentLang)) },
            label = { Text(AppLanguageManager.getString("nav_parent", currentLang)) },
            colors = itemColors
        )
        
        NavigationBarItem(
            selected = currentRoute == "ai_chat",
            onClick = {
                if (currentRoute != "ai_chat") {
                    navController.navigate("ai_chat") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = {
                Image(
                    painter = painterResource(id = co.csedge.cubycare.R.drawable.cuby_ai_robot_mother_baby),
                    contentDescription = AppLanguageManager.getString("nav_ai_chat", currentLang),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                )
            },
            label = { Text(AppLanguageManager.getString("nav_ai_chat", currentLang)) },
            colors = itemColors
        )

        NavigationBarItem(
            selected = currentRoute == "settings",
            onClick = {
                if (currentRoute != "settings") {
                    navController.navigate("settings") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            icon = { Icon(Icons.Filled.Settings, contentDescription = AppLanguageManager.getString("nav_settings", currentLang)) },
            label = { Text(AppLanguageManager.getString("nav_settings", currentLang)) },
            colors = itemColors
        )
    }
}
