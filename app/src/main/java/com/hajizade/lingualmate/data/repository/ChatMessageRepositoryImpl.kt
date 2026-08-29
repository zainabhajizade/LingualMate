//package com.hajizade.lingualmate.data.repository
//
//import com.google.firebase.auth.FirebaseAuth
//import com.hajizade.lingualmate.data.local.dao.ChatMessageDao
//import com.hajizade.lingualmate.data.local.dao.UserProfileDao
//import com.hajizade.lingualmate.data.local.entity.ChatMessageEntity
//import com.hajizade.lingualmate.data.local.entity.UserProfile
//import com.hajizade.lingualmate.domain.repository.ChatMessageRepository
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.withContext
//import javax.inject.Inject
//import com.hajizade.lingualmate.domain.model.ChatPreviewItem
//
//class ChatMessageRepositoryImpl @Inject constructor(
//    private val chatMessageDao: ChatMessageDao,
//    private val userProfileDao: UserProfileDao
//) : ChatMessageRepository {
//
//    // 👈 گرفتن UID کاربر جاری از فایربیس به صورت String (اگر لاگین نکرده باشد رشته خالی)
//    private val currentUserId: String
//        get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""
//
//    override fun getMessages(targetUserId: String): Flow<List<ChatMessageEntity>> {
//        return chatMessageDao.getMessagesBetweenUsers(currentUserId, targetUserId)
//    }
//
//    override fun getUserProfile(userId: String): Flow<UserProfile?> {
//        return userProfileDao.getUserProfileById(userId)
//    }
//
//    override suspend fun sendMessage(message: ChatMessageEntity) {
//        chatMessageDao.insertMessage(message)
//    }
//
//    override suspend fun toggleReaction(messageId: String, reaction: String) {
//        chatMessageDao.updateReaction(messageId, reaction)
//    }
//
//    override suspend fun editMessage(messageId: String, newText: String) {
//        chatMessageDao.updateMessageText(messageId, newText)
//    }
//
//    override suspend fun saveTranslation(messageId: String, translatedText: String) {
//        chatMessageDao.updateTranslation(messageId, translatedText)
//    }
//
//    override suspend fun deleteMessage(message: ChatMessageEntity) {
//        chatMessageDao.deleteMessage(message)
//    }
//
//    override suspend fun clearChat(userId: String) {
//        chatMessageDao.clearChatBetweenUsers(currentUserId, userId)
//    }
//
//    override suspend fun toggleBlockUser(userId: String, isBlocked: Boolean) {
//        userProfileDao.updateBlockStatus(userId, isBlocked)
//    }
//
//    override fun getChatPreviews(): Flow<List<ChatPreviewItem>> {
//        return chatMessageDao.getChatPreviews()
//    }
//
//    override suspend fun deleteChatWithContact(contactId: String) {
//        withContext(Dispatchers.IO) {
//            chatMessageDao.deleteMessagesWithContact(contactId)
//        }
//    }
//}
