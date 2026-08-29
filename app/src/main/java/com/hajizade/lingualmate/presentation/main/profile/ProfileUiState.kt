package com.hajizade.lingualmate.presentation.main.profile

import com.hajizade.lingualmate.data.local.entity.UserCommentEntity
import com.hajizade.lingualmate.data.local.entity.UserProfile

data class ProfileUiState(
    val isLoading: Boolean = true,
    val userProfile: UserProfile? = null,
    val comments: List<UserCommentEntity> = emptyList(),
    val errorMessage: String? = null,
    val isEditMode: Boolean = false,
    val newCommentText: String = ""
)