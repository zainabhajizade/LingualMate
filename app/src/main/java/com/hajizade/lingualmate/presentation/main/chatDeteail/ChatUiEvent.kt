package com.hajizade.lingualmate.presentation.main.chatDeteail


import android.net.Uri
import com.hajizade.lingualmate.data.local.entity.ChatMessageEntity
import com.hajizade.lingualmate.data.local.entity.MessageType

sealed interface ChatUiEvent {
    data class OnInputTextChanged(val newText: String) : ChatUiEvent
    object OnSendMessage : ChatUiEvent
    data class OnSendMedia(val uri: Uri, val fileType: String) : ChatUiEvent
    object OnStartRecordVoice : ChatUiEvent
    object OnStopRecordVoice : ChatUiEvent
    data class OnReactionSelect(val message: ChatMessageEntity, val reaction: String) : ChatUiEvent
    data class OnTranslateMessage(val message: ChatMessageEntity) : ChatUiEvent
    data class OnStartEditMessage(val message: ChatMessageEntity) : ChatUiEvent
    object OnCancelEdit : ChatUiEvent
    data class OnDeleteMessage(val message: ChatMessageEntity) : ChatUiEvent
    object OnNavigateBack : ChatUiEvent
    object OnCallClick : ChatUiEvent
    object OnVideoCallClick : ChatUiEvent
    object OnBlockUserClick : ChatUiEvent
    data class OnClearChatClick(val deleteForEveryone: Boolean) : ChatUiEvent
    data class OnUserProfileClick(val userId: String) : ChatUiEvent
}