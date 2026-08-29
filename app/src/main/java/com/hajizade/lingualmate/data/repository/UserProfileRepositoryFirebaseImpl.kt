package com.hajizade.lingualmate.data.repository

import com.hajizade.lingualmate.data.local.entity.UserProfile
import com.hajizade.lingualmate.domain.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hajizade.lingualmate.data.local.entity.UserCommentEntity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserProfileRepositoryFirebaseImpl(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : UserProfileRepository {

    override suspend fun saveUserProfile(userProfile: UserProfile) {
        val currentUserId = firebaseAuth.currentUser?.uid
        if (currentUserId != null) {
            val updatedProfile = userProfile.copy(id = currentUserId)
            firestore.collection("users").document(currentUserId).set(updatedProfile).await()
        }
    }

    override fun getUserProfile(userId: String): Flow<UserProfile?> = callbackFlow {
        val currentUserId = firebaseAuth.currentUser?.uid
        if (currentUserId.isNullOrBlank()) {
            android.util.Log.w("RepoDebug", "Current user UID is null or blank!")
            trySend(null)
            close()
            return@callbackFlow
        }

        val listener = firestore.collection("users").document(currentUserId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e("RepoDebug", "Error getting user profile", error)
                    trySend(null)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val userProfile = snapshot.toObject(UserProfile::class.java)
                    trySend(userProfile)
                } else {
                    android.util.Log.w("RepoDebug", "User document does not exist for id: $currentUserId")
                    trySend(null)
                }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun clearUserProfile() {}

    override suspend fun toggleBlockUser(userId: String, isBlocked: Boolean) {
        try {
            firestore.collection("users").document(userId)
                .update("isBlocked", isBlocked)
                .await()
        } catch (e: Exception) {
            android.util.Log.e("RepoDebug", "Error toggling block status", e)
        }
    }

    override fun getUserProfileById(userId: String): Flow<UserProfile?> = callbackFlow {
        val listener = firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(null)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val profile = snapshot.toObject(UserProfile::class.java)
                    trySend(profile)
                } else {
                    trySend(null)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getAllUsers(): List<UserProfile> {
        return try {
            val snapshot = firestore.collection("users").get().await()
            val allUsers = snapshot.toObjects(UserProfile::class.java)
            val currentEmail = firebaseAuth.currentUser?.email
            allUsers.filter { it.email != currentEmail }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun getUserComments(targetUserId: String): Flow<List<UserCommentEntity>> = callbackFlow {
        if (targetUserId.isBlank()) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listener = firestore.collection("users")
            .document(targetUserId)
            .collection("comments")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val comments = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(UserCommentEntity::class.java)
                }.sortedByDescending { it.date.toLongOrNull() ?: 0L }

                trySend(comments)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun addCommentToUser(targetUserId: String, commentText: String): Boolean {
        return try {
            val currentUserId = firebaseAuth.currentUser?.uid ?: return false
            if (currentUserId.isBlank() || commentText.isBlank()) return false

            val currentUserDoc = firestore.collection("users")
                .document(currentUserId)
                .get()
                .await()

            val authorName = currentUserDoc.getString("name") ?: "User"
            val authorAvatar = currentUserDoc.getString("profilePictureUri")

            val commentRef = firestore.collection("users")
                .document(targetUserId)
                .collection("comments")
                .document()

            val commentEntity = UserCommentEntity(
                id = commentRef.id,
                authorName = authorName,
                authorId = currentUserId,
                authorAvatarUrl = authorAvatar,
                date = System.currentTimeMillis().toString(),
                comment = commentText
            )

            commentRef.set(commentEntity).await()
            true
        } catch (_: Exception) {
            false
        }
    }

    // 👈 پیاده‌سازی متد جدید برای گرفتن یک‌جای کامنت‌ها جهت استفاده در CommunityViewModel
    override suspend fun getCommentsForUser(id: String): List<UserCommentEntity> {
        return try {
            if (id.isBlank()) return emptyList()
            val snapshot = firestore.collection("users")
                .document(id)
                .collection("comments")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(UserCommentEntity::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}