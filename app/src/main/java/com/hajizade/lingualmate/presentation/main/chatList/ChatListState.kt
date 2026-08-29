package com.hajizade.lingualmate.presentation.main.chatList

import com.hajizade.lingualmate.domain.model.ChatPreviewItem


data class ChatListState(
    val chats: List<ChatPreviewItem> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null
)