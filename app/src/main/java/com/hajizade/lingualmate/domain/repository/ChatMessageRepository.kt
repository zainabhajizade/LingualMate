package com.hajizade.lingualmate.domain.repository

import android.net.Uri
import com.hajizade.lingualmate.data.local.entity.ChatMessageEntity
import com.hajizade.lingualmate.data.local.entity.UserProfile
import com.hajizade.lingualmate.domain.model.ChatPreviewItem
import kotlinx.coroutines.flow.Flow

interface ChatMessageRepository {
    fun getMessages(targetUserId: String): Flow<List<ChatMessageEntity>>
    fun getUserProfile(userId: String): Flow<UserProfile?>
    suspend fun sendMessage(message: ChatMessageEntity)
    suspend fun toggleReaction(messageId: String, reaction: String)
    suspend fun editMessage(messageId: String, newText: String)
    suspend fun saveTranslation(messageId: String, translatedText: String)
    suspend fun deleteMessage(message: ChatMessageEntity)
    suspend fun clearChat(userId: String)
    suspend fun toggleBlockUser(userId: String, isBlocked: Boolean)
    fun getChatPreviews(): Flow<List<ChatPreviewItem>>

    suspend fun deleteChatWithContact(contactId: String)
    suspend fun sendFileMessage(receiverId: String, fileUri: Uri, fileType: String): Boolean
}