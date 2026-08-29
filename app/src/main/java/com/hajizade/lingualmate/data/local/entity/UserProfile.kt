package com.hajizade.lingualmate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName
import com.hajizade.lingualmate.domain.model.ProficiencyLevel
import com.hajizade.lingualmate.domain.model.SelectedLanguage

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val profilePictureUri: String? = null,
    val nativeLanguage: String = "",
    val targetLanguage: String = "",
    val knownLanguages: String = "",
    val interests: String = "",
    val bio: String = "",

    @get:PropertyName("online")
    @set:PropertyName("online")
    var isOnline: Boolean = false,

    @get:PropertyName("blocked")
    @set:PropertyName("blocked")
    var isBlocked: Boolean = false,

    val lastSeen: String? = null,
    val commentsCount: Int = 0
) {
    // --- توابع کمکی (بدون تغییر و کاملاً امن) ---

    fun getTargetLanguagesList(): List<SelectedLanguage> {
        if (targetLanguage.isBlank()) return emptyList()
        return targetLanguage.split(", ").mapNotNull { item ->
            val parts = item.split(":")
            if (parts.size == 2) {
                val name = parts[0]
                val level = try {
                    ProficiencyLevel.valueOf(parts[1])
                } catch (_: Exception) {
                    ProficiencyLevel.BEGINNER
                }
                SelectedLanguage(name, level)
            } else null
        }
    }

    fun getKnownLanguagesList(): List<SelectedLanguage> {
        if (knownLanguages.isBlank()) return emptyList()
        return knownLanguages.split(", ").mapNotNull { item ->
            val parts = item.split(":")
            if (parts.size == 2) {
                val name = parts[0]
                val level = try {
                    ProficiencyLevel.valueOf(parts[1])
                } catch (_: Exception) {
                    ProficiencyLevel.BEGINNER
                }
                SelectedLanguage(name, level)
            } else null
        }
    }
}

// تابع کمکی برای تبدیل لیست به String جهت ذخیره در Entity
fun List<SelectedLanguage>.toDbString(): String {
    return this.joinToString(", ") { "${it.languageName}:${it.level.name}" }
}