package com.hajizade.lingualmate.presentation.main.components


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Community : BottomNavItem(
        route = "community",
        title = "Community",
        icon = Icons.Default.People
    )

    object Chats : BottomNavItem(
        route = "chats",
        title = "Chats",
        icon = Icons.Default.Chat
    )

    object Profile : BottomNavItem(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )
}