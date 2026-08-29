package com.hajizade.lingualmate.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hajizade.lingualmate.data.local.AppDatabase
import com.hajizade.lingualmate.data.local.dao.ChatMessageDao
import com.hajizade.lingualmate.data.local.dao.UserProfileDao
import com.hajizade.lingualmate.data.repository.AuthRepositoryImpl
import com.hajizade.lingualmate.data.repository.UserProfileRepositoryFirebaseImpl
import com.hajizade.lingualmate.domain.repository.AuthRepository
import com.hajizade.lingualmate.domain.repository.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase{
        return Room.databaseBuilder(
            context = context,
            AppDatabase::class.java,
            "lingualmate_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providesUserProfileDao (db: AppDatabase) : UserProfileDao{
        return db.userProfileDao
    }

    @Provides
    fun providesChatMessageDao (db: AppDatabase) : ChatMessageDao{
        return db.chatMessageDao
    }

    // --- بخش جدید: مربوط به فایربیس (Firebase) ---
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository (
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository{
        return AuthRepositoryImpl(firebaseAuth,firestore)
    }

    @Provides
    @Singleton
    fun provideUserProfileRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): UserProfileRepository {
        return UserProfileRepositoryFirebaseImpl(firestore,firebaseAuth)
    }
}