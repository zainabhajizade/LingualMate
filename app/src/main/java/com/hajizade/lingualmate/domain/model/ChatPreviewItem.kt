package com.hajizade.lingualmate.domain.model

data class ChatPreviewItem(
    val contactId: String,
    val contactName: String,
    val contactProfilePicture: String?,
    val lastMessage: String?,
    val timestamp: Long,
    val unreadCount: Int,
    val isOnline: Boolean
)
