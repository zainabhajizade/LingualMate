package com.hajizade.lingualmate.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.hajizade.lingualmate.data.local.entity.UserProfile
import com.hajizade.lingualmate.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore // 👈 اضافه کردن فایراستور برای ساخت خودکار سند کاربر
) : AuthRepository {

    override val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid

            // ساخت خودکار پروفایل اولیه بر اساس مدل UserProfile شما
            if (userId != null) {
                val userProfile = UserProfile(
                    name = email.substringBefore("@"), // نام پیش‌فرض از روی ایمیل
                    email = email,
                    profilePictureUri = null,
                    nativeLanguage = "Persian",
                    targetLanguage = "Italian:BEGINNER",
                    knownLanguages = "English:INTERMEDIATE",
                    interests = "Language Exchange",
                    bio = "Hello! I am using LingualMate.",
                    isOnline = true,
                    isBlocked = false,
                    lastSeen = null
                )

                // ذخیره در کلکسیون users با استفاده از UID کاربر به عنوان شناسه سند
                firestore.collection("users").document(userId).set(userProfile).await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val user = authResult.user
            val userId = user?.uid

            if (userId != null) {
                // بررسی می‌کنیم که آیا این کاربر قبلاً در فایراستور سند داشته یا نه
                val docRef = firestore.collection("users").document(userId)
                val snapshot = docRef.get().await()

                if (!snapshot.exists()) {
                    // اگر برای اولین بار با گوگل وارد شد، پروفایل اولیه‌اش را بساز
                    val userProfile = UserProfile(
                        name = user.displayName ?: "Google User",
                        email = user.email ?: "",
                        profilePictureUri = user.photoUrl?.toString(),
                        nativeLanguage = "Persian",
                        targetLanguage = "Italian:BEGINNER",
                        knownLanguages = "English:INTERMEDIATE",
                        interests = "Language Exchange",
                        bio = "Hello! I am using LingualMate.",
                        isOnline = true,
                        isBlocked = false,
                        lastSeen = null
                    )
                    docRef.set(userProfile).await()
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}