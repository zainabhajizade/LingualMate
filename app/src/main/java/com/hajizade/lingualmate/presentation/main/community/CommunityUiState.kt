package com.hajizade.lingualmate.presentation.main.community


import com.hajizade.lingualmate.data.local.entity.UserProfile

data class CommunityUiState(
    val users: List<UserProfile> = emptyList(),
    val searchQuery: String = "",
    val selectedLanguageFilter: String = "All",
    val isLoading: Boolean = false,
    val error: String? = null
)