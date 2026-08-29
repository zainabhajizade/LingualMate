package com.hajizade.lingualmate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hajizade.lingualmate.data.local.entity.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    @Query("SELECT * FROM user_profile WHERE id = :id LIMIT 1")
    fun getUserProfileById(id: String): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getUserProfileOnce(): UserProfile?

    @Query("DELETE FROM user_profile")
    suspend fun clearProfile()

    // 👈 تغییر id از Int به String
    @Query("UPDATE user_profile SET isBlocked = :isBlocked WHERE id = :id")
    suspend fun updateBlockStatus(id: String, isBlocked: Boolean)

    // 👈 تغییر userId از Int به String
    @Query("SELECT * FROM user_profile WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: String): UserProfile?

    @Query("SELECT * FROM user_profile")
    suspend fun getAllUsers(): List<UserProfile>
}