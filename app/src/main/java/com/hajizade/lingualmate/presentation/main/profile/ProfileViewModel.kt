package com.hajizade.lingualmate.presentation.main.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hajizade.lingualmate.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val state: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    init {
        android.util.Log.d("ProfileDebug", "ProfileViewModel INIT block reached!")

        val userIdString = savedStateHandle.get<String>("userId")
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        val targetId = if (!userIdString.isNullOrEmpty()) userIdString else currentUserId

        if (!targetId.isNullOrEmpty()) {
            // لود کردن کامنت‌ها برای کاربری که پروفایلش در حال نمایش است
            loadComments(targetId)
        }

        if (!userIdString.isNullOrEmpty()) {
            android.util.Log.d("ProfileDebug", "Loading other user profile with ID: $userIdString")
            loadOtherUserProfile(userIdString)
        } else if (!currentUserId.isNullOrEmpty()) {
            android.util.Log.d("ProfileDebug", "Loading current user profile with ID: $currentUserId")
            loadCurrentUserData(currentUserId)
        } else {
            android.util.Log.d("ProfileDebug", "No valid user found, stopping loading.")
            _uiState.update { it.copy(isLoading = false) }
        }
    }


    // 👈 اصلاح شد: دریافت currentUserId به عنوان ورودی
    private fun loadCurrentUserData(currentUserId: String) {
        viewModelScope.launch {
            try {
                // اگر متد getUserProfile بدون آرگومان است، می‌توانید آن را به getUserProfile() تغییر دهید
                // یا شناسه کاربر را مانند زیر پاس دهید:
                userProfileRepository.getUserProfile(currentUserId).collect { profile ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            userProfile = profile,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadOtherUserProfile(userId: String) {
        viewModelScope.launch {
            try {
                userProfileRepository.getUserProfileById(userId).collect { profile ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            userProfile = profile,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadComments(targetUserId: String) {
        viewModelScope.launch {
            userProfileRepository.getUserComments(targetUserId).collect { commentsList ->
                _uiState.update { it.copy(comments = commentsList) }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.SaveProfile -> {
                viewModelScope.launch {
                    _uiState.value.userProfile?.let { profileToSave ->
                        userProfileRepository.saveUserProfile(profileToSave)
                    }
                }
            }
            is ProfileEvent.UpdateName -> {
                _uiState.update { it.copy(userProfile = it.userProfile?.copy(name = event.name)) }
            }
            is ProfileEvent.UpdateBio -> {
                _uiState.update { it.copy(userProfile = it.userProfile?.copy(bio = event.bio)) }
            }
            is ProfileEvent.UpdateInterests -> {
                _uiState.update { it.copy(userProfile = it.userProfile?.copy(interests = event.interests)) }
            }
            is ProfileEvent.UpdateKnownLanguages -> {
                _uiState.update { it.copy(userProfile = it.userProfile?.copy(knownLanguages = event.languages)) }
            }
            is ProfileEvent.UpdateNativeLanguage -> {
                _uiState.update { it.copy(userProfile = it.userProfile?.copy(nativeLanguage = event.language)) }
            }
            is ProfileEvent.UpdateProfilePicture -> {
                _uiState.update { it.copy(userProfile = it.userProfile?.copy(profilePictureUri = event.uri)) }
            }
            is ProfileEvent.UpdateTargetLanguage -> {
                _uiState.update {
                    it.copy(
                        userProfile = it.userProfile?.copy(targetLanguage = event.language)
                    )
                }
            }
            is ProfileEvent.OnCommentChanged -> {
                _uiState.update { it.copy(newCommentText = event.text) }
            }

            is ProfileEvent.SubmitComment -> {
                viewModelScope.launch {
                    val currentText = _uiState.value.newCommentText
                    val targetUserId = _uiState.value.userProfile?.id ?: _uiState.value.userProfile?.id

                    if (currentText.isNotBlank() && targetUserId != null) {
                        val success = userProfileRepository.addCommentToUser(targetUserId, currentText)
                        if (success) {
                            _uiState.update { it.copy(newCommentText = "") }
                        }
                    }
                }
            }
            ProfileEvent.OnMoreClick -> {}
            ProfileEvent.StartAudioCall -> {}
            ProfileEvent.StartVideoCall -> {}
        }
    }
}