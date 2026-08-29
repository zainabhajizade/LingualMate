package com.hajizade.lingualmate.domain.repository

interface AuthRepository {
    fun isUserLoggedIn(): Boolean

    // گرفتن شناسه یکتای کاربر فعلی (UID)
    val currentUserId: String?

    suspend fun signInWithEmail(email: String, password: String): Result<Unit>
    suspend fun signUpWithEmail(email: String, password: String): Result<Unit>

    suspend fun signInWithGoogle(idToken: String): Result<Unit>
    // خروج از حساب کاربری
    fun signOut()
}