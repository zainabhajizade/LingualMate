package com.hajizade.lingualmate.presentation.main.profile

sealed interface ProfileEvent {
    data class UpdateName(val name: String) : ProfileEvent
    data class UpdateBio(val bio: String) : ProfileEvent
    data class UpdateProfilePicture(val uri: String?) : ProfileEvent
    data class UpdateNativeLanguage(val language: String) : ProfileEvent
    data class UpdateTargetLanguage(val language: String) : ProfileEvent
    data class UpdateKnownLanguages(val languages: String) : ProfileEvent
    data class UpdateInterests(val interests: String) : ProfileEvent

    object SaveProfile : ProfileEvent
    object OnMoreClick : ProfileEvent
    object StartAudioCall : ProfileEvent //👈 ایونت تماس صوتی
    object StartVideoCall : ProfileEvent

    data class OnCommentChanged(val text: String) : ProfileEvent
    object SubmitComment : ProfileEvent
}