package com.hajizade.lingualmate.data.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.hajizade.lingualmate.data.local.entity.ChatMessageEntity
import com.hajizade.lingualmate.data.local.entity.MessageStatus
import com.hajizade.lingualmate.data.local.entity.MessageType
import com.hajizade.lingualmate.data.local.entity.UserProfile
import com.hajizade.lingualmate.domain.model.ChatPreviewItem
import com.hajizade.lingualmate.domain.repository.ChatMessageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatMessageRepositoryFirebaseImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : ChatMessageRepository {

    private val currentUserId: String
        get() = firebaseAuth.currentUser?.uid ?: ""

    private fun getChatRoomId(userId1: String, userId2: String): String {
        return if (userId1 < userId2) {
            "${userId1}_$userId2"
        } else {
            "${userId2}_$userId1"
        }
    }

    override fun getUserProfile(userId: String): Flow<UserProfile?> = callbackFlow {
        if (userId.isBlank()) {
            trySend(null)
            close()
            return@callbackFlow
        }

        val listener = firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    try {
                        val profile = snapshot.toObject(UserProfile::class.java)
                        trySend(profile)
                    } catch (_: Exception) {
                        trySend(null)
                    }
                } else {
                    trySend(null)
                }
            }
        awaitClose { listener.remove() }
    }

    override fun getMessages(targetUserId: String): Flow<List<ChatMessageEntity>> = callbackFlow {
        val chatRoomId = getChatRoomId(currentUserId, targetUserId)

        val listener = firestore.collection("chats")
            .document(chatRoomId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val messages = snapshot.toObjects(ChatMessageEntity::class.java).map { message ->
                        message.copy(isFromMe = message.senderId == currentUserId)
                    }
                    trySend(messages)
                } else {
                    trySend(emptyList())
                }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun sendMessage(message: ChatMessageEntity) {
        try {
            val chatRoomId = getChatRoomId(message.senderId, message.receiverId)
            val chatRoomRef = firestore.collection("chats").document(chatRoomId)

            // ۱. ذخیره اطلاعات آخرین پیام در داکیومنت اصلی چت
            val chatRoomData = mapOf(
                "lastMessage" to message.text,
                "timestamp" to message.timestamp,
                "users" to listOf(message.senderId, message.receiverId) // اختیاری برای کوئری‌های آینده
            )
            chatRoomRef.set(chatRoomData, com.google.firebase.firestore.SetOptions.merge()).await()

            // ۲. ذخیره پیام در ساب‌کلکشن messages
            chatRoomRef.collection("messages")
                .document(message.id)
                .set(message)
                .await()
        } catch (_: Exception) {}
    }

    override suspend fun toggleReaction(messageId: String, reaction: String) {
        try {
            val querySnapshot = firestore.collectionGroup("messages")
                .whereEqualTo("id", messageId)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                document.reference.update("reactions", reaction).await()
            }
        } catch (_: Exception) {}
    }

    override suspend fun editMessage(messageId: String, newText: String) {
        try {
            val querySnapshot = firestore.collectionGroup("messages")
                .whereEqualTo("id", messageId)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                document.reference.update(
                    mapOf(
                        "text" to newText,
                        "edited" to true
                    )
                ).await()
            }
        } catch (_: Exception) {}
    }

    override suspend fun saveTranslation(messageId: String, translatedText: String) {
        try {
            val querySnapshot = firestore.collectionGroup("messages")
                .whereEqualTo("id", messageId)
                .get()
                .await()

            for (document in querySnapshot.documents) {
                document.reference.update(
                    mapOf(
                        "translatedText" to translatedText,
                        "translated" to true
                    )
                ).await()
            }
        } catch (_: Exception) {}
    }

    override suspend fun deleteMessage(message: ChatMessageEntity) {
        try {
            val chatRoomId = getChatRoomId(message.senderId, message.receiverId)
            firestore.collection("chats")
                .document(chatRoomId)
                .collection("messages")
                .document(message.id)
                .delete()
                .await()
        } catch (_: Exception) {}
    }

    override suspend fun clearChat(userId: String) {
        try {
            val chatRoomId = getChatRoomId(currentUserId, userId)
            val messagesRef = firestore.collection("chats")
                .document(chatRoomId)
                .collection("messages")
                .get()
                .await()

            val batch = firestore.batch()
            for (document in messagesRef.documents) {
                batch.delete(document.reference)
            }
            batch.commit().await()
        } catch (_: Exception) {}
    }

    override suspend fun toggleBlockUser(userId: String, isBlocked: Boolean) {
        try {
            firestore.collection("users").document(userId)
                .update("isBlocked", isBlocked)
                .await()
        } catch (_: Exception) {}
    }

    // ۱۰. دریافت لیست پیش‌نمایش چت‌ها (فقط کاربرانی که با آن‌ها چت شده است)
    override fun getChatPreviews(): Flow<List<ChatPreviewItem>> = callbackFlow {
        if (currentUserId.isEmpty()) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        // استفاده از whereArrayContains برای گرفتن چت‌هایی که کاربر در آن‌ها حضور دارد
        val listener = firestore.collection("chats")
            .whereArrayContains("users", currentUserId)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val previewList = mutableListOf<ChatPreviewItem>()

                    for (document in snapshot.documents) {
                        val chatRoomId = document.id
                        val userIds = chatRoomId.split("_")
                        val otherUserId = userIds.firstOrNull { it != currentUserId } ?: continue

                        val lastMessage = document.getString("lastMessage") ?: "No messages yet"
                        val timestamp = document.getLong("timestamp") ?: 0L

                        try {
                            val userDoc = firestore.collection("users")
                                .document(otherUserId)
                                .get()
                                .await()

                            val userProfile = userDoc.toObject(UserProfile::class.java)

                            val previewItem = ChatPreviewItem(
                                contactId = otherUserId,
                                contactName = userProfile?.name ?: "Unknown",
                                contactProfilePicture = userProfile?.profilePictureUri,
                                lastMessage = lastMessage,
                                timestamp = timestamp,
                                unreadCount = 0,
                                isOnline = false
                            )
                            previewList.add(previewItem)
                        } catch (_: Exception) {}
                    }

                    val sortedList = previewList.sortedByDescending { it.timestamp }
                    trySend(sortedList)
                }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun deleteChatWithContact(contactId: String) {
        withContext(Dispatchers.IO) {
            clearChat(contactId)
        }
    }
    override suspend fun sendFileMessage(
        receiverId: String,
        fileUri: Uri,
        fileType: String
    ): Boolean {
        return try {
            android.util.Log.d("UploadDebug", "Starting file upload for URI: $fileUri, Type: $fileType")

            val inputStream = context.contentResolver.openInputStream(fileUri)
                ?: throw Exception("InputStream is null for Uri: $fileUri")
            val bytes = inputStream.readBytes()
            inputStream.close()

            val fileName = "${System.currentTimeMillis()}_${fileUri.lastPathSegment ?: "file"}"
            val storageRef = FirebaseStorage.getInstance()
                .reference
                .child("chat_files/$currentUserId/$fileName")

            android.util.Log.d("UploadDebug", "Uploading bytes to path: ${storageRef.path}")
            storageRef.putBytes(bytes).await()

            val downloadUrl = storageRef.downloadUrl.await().toString()
            android.util.Log.d("UploadDebug", "Upload successful! Download URL: $downloadUrl")

            val messageType = when (fileType.lowercase()) {
                "image" -> MessageType.IMAGE
                "video" -> MessageType.VIDEO
                "audio" -> MessageType.AUDIO_FILE
                else -> MessageType.DOCUMENT
            }

            val message = ChatMessageEntity(
                id = java.util.UUID.randomUUID().toString(),
                senderId = currentUserId,
                receiverId = receiverId,
                text = null,
                mediaUrl = downloadUrl,
                fileName = fileUri.lastPathSegment ?: "file",
                fileSize = null,
                duration = null,
                isFromMe = true,
                reactions = "",
                type = messageType,
                status = MessageStatus.SENT,
                isEdited = false,
                isDeletedForMe = false,
                isDeletedForEveryone = false,
                isTranslated = false,
                translatedText = null,
                timestamp = System.currentTimeMillis()
            )

            sendMessage(message)
            true
        } catch (e: Exception) {
            // 👈 لطفاً متنی که در این لاگ چاپ می‌شود را بررسی کنید
            android.util.Log.e("UploadDebug", "Failed to upload: ${e.localizedMessage}", e)
            false
        }
    }
}