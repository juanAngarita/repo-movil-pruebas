package com.example.twitterfalso.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.twitterfalso.ui.functions.Utils
import com.example.twitterfalso.ui.theme.TwitterFalsoTheme


data class BottomNavItem(
    val filledIcon: ImageVector,
    val outlineIcon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem(Icons.Filled.Home, Icons.Outlined.Home, Screen.Home.route),
    BottomNavItem(Icons.Filled.Search, Icons.Outlined.Search, Screen.Search.route),
    BottomNavItem(Icons.Filled.Notifications, Icons.Outlined.Notifications, Screen.Notifications.route),
    BottomNavItem(Icons.Filled.Email, Icons.Outlined.Email, Screen.Settings.route),
    BottomNavItem(Icons.Filled.Person, Icons.Outlined.Person, Screen.Profile.route),
)

@Composable
fun TwitterBottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
){

    val currentRoute = Utils.getCurrentRoute(navController)

    NavigationBar(
        modifier = modifier
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = (currentRoute == item.route)
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.filledIcon else item.outlineIcon,
                        contentDescription = item.route
                    )
                },
                selected = false,
                onClick = {
                    navController.navigate(item.route)
                }
            )
        }
    }
}

@Preview
@Composable
fun TwitterBottomNavigationBarPreview() {
    TwitterFalsoTheme {
        TwitterBottomNavigationBar(
            navController = rememberNavController()
        )
    }

}

@Preview
@Composable
fun TwitterBottomNavigationBarPreviewDark() {
    TwitterFalsoTheme(
        darkTheme = true
    ) {
        TwitterBottomNavigationBar(
            navController = rememberNavController()
        )
    }

}