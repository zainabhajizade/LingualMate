package com.hajizade.lingualmate.presentation.main.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainBottomNavigationBar(
    currentRoute: String,
    onTabSelected: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Community,
        BottomNavItem.Chats,
        BottomNavItem.Profile
    )

    NavigationBar(
        modifier = modifier,
        // رنگ پس‌زمینه کلی نوار (می‌توانید از surfaceVariant یا رنگ دلخواه روشن‌تر استفاده کنید)
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(text = item.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    // ۱. رنگ آیکون وقتی انتخاب شده (رنگی / Primary)
                    selectedIconColor = MaterialTheme.colorScheme.primary,

                    // ۲. رنگ متن وقتی انتخاب شده (رنگی / Primary)
                    selectedTextColor = MaterialTheme.colorScheme.primary,

                    // ۳. رنگ پس‌زمینه (بیضی) پشت آیکون هنگام انتخاب (یک رنگ بسیار روشن و ملایم)
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),

                    // ۴. رنگ آیکون و متن وقتی انتخاب نشده‌اند (خاکستری یا ملایم)
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}