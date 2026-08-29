package com.hajizade.lingualmate.presentation.main.chatList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hajizade.lingualmate.presentation.main.chatList.components.ChatItemRow
import com.hajizade.lingualmate.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    viewModel: ChatListViewModel = hiltViewModel(),
    onChatClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ۱. نوار جستجو (Search Bar) با استفاده از رنگ‌های استاندارد متریال 3
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { query ->
                viewModel.onEvent(ChatListEvent.OnSearchQueryChanged(query))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            placeholder = { Text("Search", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = MaterialTheme.colorScheme.onSurfaceVariant) },
            shape = RoundedCornerShape(24.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )

        // ۲. فیلتر کردن لیست بر اساس سرچ کاربر
        val filteredChats = state.chats.filter { chat ->
            chat.contactName.contains(state.searchQuery, ignoreCase = true)
        }

        // ۳. لیست گفتگوها متصل به دیتابیس
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(
                items = filteredChats,
                key = { it.contactId }
            ) { chatPreview ->
                ChatItemRow(
                    chat = chatPreview,
                    onClick = { onChatClick(chatPreview.contactId) }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    thickness = 0.8.dp
                )
            }
        }
    }
}