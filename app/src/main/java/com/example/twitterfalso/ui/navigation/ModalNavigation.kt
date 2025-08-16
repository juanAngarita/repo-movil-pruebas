package com.example.twitterfalso.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.twitterfalso.ui.utils.ProfileAsyncImage

data class DrawerItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

val drawerItems = listOf(
    DrawerItem("Perfil", Icons.Default.Person, "profile"),
    DrawerItem("Premium", Icons.Default.Star, "premium"),
    DrawerItem("Chat", Icons.Default.Chat, "chat"),
    DrawerItem("Elementos guardados", Icons.Default.Bookmark, "saved"),
    DrawerItem("Empleos", Icons.Default.Work, "jobs"),
    DrawerItem("Listas", Icons.Default.List, "lists"),
    DrawerItem("Espacios", Icons.Default.Mic, "spaces"),
    DrawerItem("Monetización", Icons.Default.AttachMoney, "monetization")
)

@Composable
fun ModalNavigation(
    imageProfile: String,
    onProfileClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    ModalDrawerSheet {
        IconButton(
            onClick = onProfileClicked,
            modifier = Modifier.padding(8.dp)
        ) {
            ProfileAsyncImage(
                profileImage = imageProfile,
                size = 50
            )
        }
        HorizontalDivider()
        drawerItems.forEach { item ->
            NavigationDrawerItem(
                label = { Text(text = item.title) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                selected = false,
                onClick = { /*TODO*/ }
            )
        }
    }
}