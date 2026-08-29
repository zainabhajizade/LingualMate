package com.hajizade.lingualmate.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hajizade.lingualmate.data.local.entity.ChatMessageEntity
import com.hajizade.lingualmate.domain.model.ChatPreviewItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {

    // کوئری مکالمه دوطرفه بین کاربر جاری و شخص مقابل
    @Query("""
        SELECT * FROM chat_messages 
        WHERE (senderId = :currentUserId AND receiverId = :targetUserId) 
           OR (senderId = :targetUserId AND receiverId = :currentUserId) 
        ORDER BY timestamp ASC
    """)
    fun getMessagesBetweenUsers(currentUserId: String, targetUserId: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Update
    suspend fun updateMessage(message: ChatMessageEntity)

    @Delete
    suspend fun deleteMessage(message: ChatMessageEntity)

    // 👈 تغییر از Int به String
    @Query("DELETE FROM chat_messages WHERE (senderId = :currentUserId AND receiverId = :targetUserId) OR (senderId = :targetUserId AND receiverId = :currentUserId)")
    suspend fun clearChatBetweenUsers(currentUserId: String, targetUserId: String)

    // ثبت/ویرایش ری‌اکشن روی یک پیام خاص
    @Query("UPDATE chat_messages SET reactions = :reaction WHERE id = :messageId")
    suspend fun updateReaction(messageId: String, reaction: String?)

    // ویرایش متن پیام
    @Query("UPDATE chat_messages SET text = :newText WHERE id = :messageId")
    suspend fun updateMessageText(messageId: String, newText: String)

    // ذخیره متن ترجمه‌شده
    @Query("UPDATE chat_messages SET translatedText = :translatedText WHERE id = :messageId")
    suspend fun updateTranslation(messageId: String, translatedText: String)

    // کوئری برای گرفتن آخرین پیام‌ها و اطلاعات مخاطبین برای صفحه لیست چت
    @Query("""
        SELECT 
            u.id AS contactId,
            u.name AS contactName,
            u.profilePictureUri AS contactProfilePicture,
            m.text AS lastMessage,
            m.timestamp AS timestamp,
            u.isOnline AS isOnline,
            (SELECT COUNT(*) FROM chat_messages WHERE senderId = u.id AND status != 'READ') AS unreadCount
        FROM user_profile u
        INNER JOIN chat_messages m ON m.senderId = u.id OR m.receiverId = u.id
        GROUP BY u.id
        ORDER BY m.timestamp DESC
    """)
    fun getChatPreviews(): Flow<List<ChatPreviewItem>>

    // 👈 تغییر از Int به String
    @Query("DELETE FROM chat_messages WHERE senderId = :contactId OR receiverId = :contactId")
    suspend fun deleteMessagesWithContact(contactId: String)
}