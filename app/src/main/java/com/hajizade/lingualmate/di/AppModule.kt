package com.hajizade.lingualmate.di

import com.hajizade.lingualmate.data.repository.ChatMessageRepositoryFirebaseImpl
import com.hajizade.lingualmate.domain.repository.ChatMessageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    // ❌ تابع bindUserRepository مربوط به Room را پاک کردیم تا با فایربیس تداخل نکند
    // (نسخه فایربیس آن در DatabaseModule تامین می‌شود)

    // ✅ بخش چت همچنان سر جای خودش محفوظ می‌ماند
    @Module
    @InstallIn(SingletonComponent::class)
    abstract class RepositoryModule {

        @Binds
        abstract fun bindChatMessageRepository(
            chatMessageRepositoryImpl: ChatMessageRepositoryFirebaseImpl
        ): ChatMessageRepository
    }
}