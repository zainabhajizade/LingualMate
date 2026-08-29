package com.hajizade.lingualmate.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hajizade.lingualmate.presentation.main.chatList.ChatListScreen
import com.hajizade.lingualmate.presentation.main.community.CommunityScreen
import com.hajizade.lingualmate.presentation.main.components.BottomNavItem
import com.hajizade.lingualmate.presentation.main.components.MainBottomNavigationBar
import com.hajizade.lingualmate.presentation.main.profile.ProfileScreen

@Composable
fun MainScreen(
    onNavigateToEditProfile: () -> Unit,
    onNavigateToChat: (String) -> Unit,
    onNavigateToUserProfile: (String) -> Unit
) {
    val bottomNavController = rememberNavController()

    // 👈 خواندن مسیر فعلی از روی NavController داخلی
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: BottomNavItem.Community.route

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            MainBottomNavigationBar(
                currentRoute = currentRoute, // 👈 ارسال مسیر واقعی به نوار پایین
                onTabSelected = { selectedTab ->
                    bottomNavController.navigate(selectedTab.route) {
                        popUpTo(bottomNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Community.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Community.route) {
                CommunityScreen(
                    onUserClick = { userId ->
                        onNavigateToUserProfile(userId)
                    }
                )
            }
            composable(BottomNavItem.Chats.route) {
                ChatListScreen(
                    onChatClick = { contactId ->
                        onNavigateToChat(contactId)
                    }
                )
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    onEditProfile = onNavigateToEditProfile,
                    modifier = Modifier.fillMaxSize(),
                    onNavigationToUser = { targetUserId ->
                        onNavigateToUserProfile(targetUserId)
                    }
                )
            }
        }
    }
}