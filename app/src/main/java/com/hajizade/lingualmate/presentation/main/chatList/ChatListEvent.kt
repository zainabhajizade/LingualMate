package com.hajizade.lingualmate.presentation.main.chatList

sealed interface ChatListEvent {
    data class OnChatClicked(val contactId: String) : ChatListEvent
    data class OnSearchQueryChanged(val query: String) : ChatListEvent
    data class OnDeleteChatClicked(val contactId: String) : ChatListEvent // اضافه شد
}