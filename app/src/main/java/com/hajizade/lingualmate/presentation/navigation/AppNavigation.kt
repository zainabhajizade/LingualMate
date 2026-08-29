package com.hajizade.lingualmate.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth // 👈 برای چک کردن وضعیت لاگین فایربیس
import com.hajizade.lingualmate.presentation.auth.LoginScreen
import com.hajizade.lingualmate.presentation.main.MainScreen
import com.hajizade.lingualmate.presentation.main.chatDeteail.ChatDetailScreen
import com.hajizade.lingualmate.presentation.main.chatList.ChatListScreen
import com.hajizade.lingualmate.presentation.main.community.CommunityScreen
import com.hajizade.lingualmate.presentation.main.profile.EditProfileScreen
import com.hajizade.lingualmate.presentation.main.userProfile.UserProfileScreen
import com.hajizade.lingualmate.presentation.onBoarding.OnboardingScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // بررسی اینکه آیا کاربر از قبل در فایربیس لاگین کرده است یا خیر
    val isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null

    // اگر لاگین بود مستقیم به Main یا Onboarding می‌رود، وگرنه به صفحه Login
    val startDestination = if (isUserLoggedIn) Screen.Main.route else Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ۰. صفحه ورود و ثبت‌نام (Login)
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = hiltViewModel(),
                onLoginSuccess = {
                    // بعد از لاگین موفق، هدایت به آنبردینگ و پاک کردن صفحه لاگین از پشته
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // ۱. صفحه Onboarding
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                viewModel = hiltViewModel(),
                onFinishOnboarding = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // ۲. صفحه اصلی (Main)
        composable(Screen.Main.route) {
            MainScreen(
                onNavigateToEditProfile = {
                    navController.navigate(Screen.EditProfile.route)
                },
                onNavigateToChat = { userId ->
                    navController.navigate(Screen.ChatDetail.createRoute(userId))
                },
                onNavigateToUserProfile = { userId ->
                    // 👈 اتصال کلیک کامنت در پروفایل شخصی به ناوبری پروفایل دیگران
                    navController.navigate(Screen.UserProfile.createRoute(userId))
                }
            )
        }

        // ۳. صفحه ویرایش پروفایل
        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogoutSuccess = {
                    navController.navigate("login_screen") {
                        // پاک کردن تمام استک قبلی تا کاربر نتواند با Back برگردد
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // ۴. صفحه جزئیات چت
        composable(
            route = Screen.ChatDetail.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            ChatDetailScreen(
                onNavigateBack = {
                    // 👈 برگشت استاندارد به صفحه قبلی در پشته ناوبری
                    navController.popBackStack()
                },
                onNavigateToUserProfile = { userId ->
                    // هدایت به صفحه پروفایل کاربر مقابل
                    navController.navigate(Screen.UserProfile.createRoute(userId))
                }
            )
        }

        // ۵. صفحه پروفایل کاربر دیگر
        composable(
            route = Screen.UserProfile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            // استخراج userId از مسیر (arguments) برای استفاده در دکمه پیام
            val userId = backStackEntry.arguments?.getString("userId") ?: ""

            UserProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onMessageClick = {
                    // 👈 هدایت به صفحه چت با همین کاربر و حذف صفحه پروفایل از پشته (اختیاری)
                    navController.navigate(Screen.ChatDetail.createRoute(userId)) {
                         popUpTo(Screen.UserProfile.route) { inclusive = true }
                    }
                },
                onNavigationToUser = { targetUserId ->
                    navController.navigate(Screen.UserProfile.createRoute(targetUserId))
                }
            )
        }
        // ۶. صفحه جامعه کاربران (Community)
        composable(route = Screen.Community.route) {
            CommunityScreen(
                onUserClick = { userIdString ->
                    navController.navigate(Screen.UserProfile.createRoute(userIdString))
                }
            )
        }

        // ۷. صفحه لیست چت‌ها (ChatList)
        composable(route = Screen.ChatList.route) {
            ChatListScreen(
                onChatClick = { contactId ->
                    navController.navigate(Screen.ChatDetail.createRoute(contactId))
                }
            )
        }
    }
}