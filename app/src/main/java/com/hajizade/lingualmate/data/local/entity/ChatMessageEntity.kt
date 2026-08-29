package com.hajizade.lingualmate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MessageType {
    TEXT, VOICE, IMAGE, VIDEO, AUDIO_FILE, DOCUMENT
}

enum class MessageStatus {
    SENDING, SENT, DELIVERED, READ // SENT = ۱ تیک، READ = ۲ تیک آبی/سبز
}

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val text: String? = null,
    val mediaUrl: String? = null,
    val fileName: String? = null,
    val fileSize: String? = null,
    val duration: String? = null,
    val timestamp: Long = 0L,
    val isFromMe: Boolean = false,
    val reactions: String = "",
    val type: MessageType = MessageType.TEXT,
    val status: MessageStatus = MessageStatus.SENT, // وضعیت تیک‌ها
    val isEdited: Boolean = false,
    val isDeletedForMe: Boolean = false,        // حذف یک‌طرفه
    val isDeletedForEveryone: Boolean = false,  // حذف دوطرفه
    val isTranslated: Boolean = false,
    val translatedText: String? = null
)