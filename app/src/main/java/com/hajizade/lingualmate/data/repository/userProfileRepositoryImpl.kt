package com.hajizade.lingualmate.data.repository

import com.hajizade.lingualmate.data.local.dao.UserProfileDao
import com.hajizade.lingualmate.data.local.entity.UserProfile
import com.hajizade.lingualmate.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//class UserProfileRepositoryImpl @Inject constructor(
//    private val userProfileDao: UserProfileDao
//) : UserProfileRepository {
//
//    override suspend fun saveUserProfile(userProfile: UserProfile) {
//        userProfileDao.insertOrUpdateProfile(userProfile)
//    }
//
//    // اگر کاربر جاری را هم بر اساس Firebase UID می‌خواهید بگیرید:
//    override fun getUserProfile(): Flow<UserProfile?> {
//        // اینجا باید UID کاربر جاری فایربیس را پاس بدهید یا از متد مناسب استفاده کنید
//        val currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
//        return userProfileDao.getUserById(currentUserId)
//    }
//
//    override suspend fun clearUserProfile() {
//        userProfileDao.clearProfile()
//    }
//
//    // 👈 اصلاح نوع userId به String برای هماهنگی با فایربیس
//    override suspend fun toggleBlockUser(userId: String, isBlocked: Boolean) {
//        userProfileDao.updateBlockStatus(userId, isBlocked)
//    }
//
//    // 👈 حذف کلمه suspend چون Flow نیازی به suspend ندارد
//    override fun getUserProfileById(userId: String): Flow<UserProfile?> {
//        return userProfileDao.getUserById(userId)
//    }
//
//    override suspend fun getAllUsers(): List<UserProfile> {
//        return userProfileDao.getAllUsers()
//    }
//}