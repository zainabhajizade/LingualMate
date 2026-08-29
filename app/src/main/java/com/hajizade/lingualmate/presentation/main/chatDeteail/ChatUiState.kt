package com.hajizade.lingualmate.presentation.main.chatDeteail


import com.hajizade.lingualmate.data.local.entity.ChatMessageEntity
import com.hajizade.lingualmate.data.local.entity.UserProfile

data class ChatUiState(
    val messages: List<ChatMessageEntity> = emptyList(),
    val receiverUser: UserProfile? = null,
    val inputText: String = "",
    val editingMessageId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isBlocked : Boolean = false,
    val isRecording: Boolean = false
)