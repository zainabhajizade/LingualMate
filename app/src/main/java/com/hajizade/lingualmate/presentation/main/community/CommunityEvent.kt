package com.hajizade.lingualmate.presentation.main.community


sealed interface CommunityEvent {
    data class OnUserClicked(val userId: String) : CommunityEvent
    data class OnSearchQueryChanged(val query: String) : CommunityEvent
    data class OnLanguageFilterSelected(val language: String) : CommunityEvent
}