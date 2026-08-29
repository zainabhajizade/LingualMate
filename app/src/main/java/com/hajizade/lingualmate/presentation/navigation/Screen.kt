package com.hajizade.lingualmate.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Onboarding : Screen("onboarding_screen")
    object Main : Screen("main_screen")
    object EditProfile : Screen("edit_profile_screen")

    object ChatDetail : Screen("chat_detail_screen/{userId}") {
        fun createRoute(userId: String) = "chat_detail_screen/$userId"
    }

    object UserProfile : Screen("user_profile_screen/{userId}") {
        fun createRoute(userId: String) = "user_profile_screen/$userId"
    }

    object Community : Screen("community_screen")
    object ChatList : Screen(route = "chatList_screen")
}