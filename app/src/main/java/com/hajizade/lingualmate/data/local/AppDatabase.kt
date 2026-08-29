package com.hajizade.lingualmate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hajizade.lingualmate.data.local.dao.ChatMessageDao
import com.hajizade.lingualmate.data.local.dao.UserProfileDao
import com.hajizade.lingualmate.data.local.entity.ChatMessageEntity
import com.hajizade.lingualmate.data.local.entity.UserProfile

@Database(
    entities = [UserProfile::class, ChatMessageEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    abstract val userProfileDao : UserProfileDao
    abstract val chatMessageDao: ChatMessageDao
}