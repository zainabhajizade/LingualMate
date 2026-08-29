package com.hajizade.lingualmate.domain.repository

import com.hajizade.lingualmate.data.local.entity.UserCommentEntity
import com.hajizade.lingualmate.data.local.entity.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository{
    suspend fun saveUserProfile(userProfile: UserProfile)
    fun getUserProfile(userId: String): Flow<UserProfile?>
    suspend fun clearUserProfile()
    suspend fun toggleBlockUser(userId: String, isBlocked: Boolean)
    fun getUserProfileById(userId: String): Flow<UserProfile?>
    suspend fun getAllUsers(): List<UserProfile>
    fun getUserComments(targetUserId: String): Flow<List<UserCommentEntity>>
    suspend fun addCommentToUser(targetUserId: String, commentText: String): Boolean
    suspend fun getCommentsForUser(id: String): List<UserCommentEntity>

}