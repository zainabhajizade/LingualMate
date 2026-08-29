package com.hajizade.lingualmate.presentation.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hajizade.lingualmate.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        // شروع تایمر کارت‌ها به محض ساخته شدن ViewModel
        startIntroTimer()
    }

    private fun startIntroTimer() {
        viewModelScope.launch {
            for (cardIndex in 0..2) {
                _uiState.update {
                    it.copy(
                        currentStep = OnboardingStep.Intro(cardIndex = cardIndex)
                    )
                }
                delay(2700L) // هماهنگ با زمان کل انیمیشن کارت
            }

            // رفتن به بخش سوالات
            _uiState.update {
                it.copy(
                    currentStep = OnboardingStep.Question(questionIndex = 0)
                )
            }
        }
    }

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            OnboardingEvent.NextIntroCard -> {} // تایمر خودکار است
            OnboardingEvent.NextQuestion -> {
                _uiState.update { currentState ->
                    val currentStep = currentState.currentStep
                    if (currentStep is OnboardingStep.Question) {
                        currentState.copy(
                            currentStep = OnboardingStep.Question(questionIndex = currentStep.questionIndex + 1)
                        )
                    } else {
                        currentState
                    }
                }
            }
            OnboardingEvent.PreviousQuestion -> {
                _uiState.update { currentState ->
                    val currentStep = currentState.currentStep
                    if (currentStep is OnboardingStep.Question && currentStep.questionIndex > 0) {
                        currentState.copy(
                            currentStep = OnboardingStep.Question(questionIndex = currentStep.questionIndex - 1)
                        )
                    } else {
                        currentState
                    }
                }
            }
            OnboardingEvent.SubmitOnboarding -> {
                viewModelScope.launch {
                    userProfileRepository.saveUserProfile(_uiState.value.userProfile)
                    _uiState.update {
                        it.copy(isCompleted = true)
                    }
                }
            }
            is OnboardingEvent.UpdateBio -> {
                _uiState.update {
                    it.copy(
                        userProfile = it.userProfile.copy(
                            bio = event.bio
                        )
                    )
                }
            }
            is OnboardingEvent.UpdateEmail -> {
                _uiState.update {
                    it.copy(
                        userProfile = it.userProfile.copy(
                            email = event.email
                        )
                    )
                }
            }
            is OnboardingEvent.UpdateInterests -> {
                _uiState.update {
                    it.copy(
                        userProfile = it.userProfile.copy(
                            interests = event.interests
                        )
                    )
                }
            }
            is OnboardingEvent.UpdateKnownLanguages -> {
                _uiState.update {
                    it.copy(
                        userProfile = it.userProfile.copy(
                            knownLanguages = event.languages
                        )
                    )
                }
            }
            is OnboardingEvent.UpdateName -> {
                _uiState.update {
                    it.copy(
                        userProfile = it.userProfile.copy(
                            name = event.name
                        )
                    )
                }
            }
            is OnboardingEvent.UpdateNativeLanguage -> {
                _uiState.update {
                    it.copy(
                        userProfile = it.userProfile.copy(
                            nativeLanguage = event.language
                        )
                    )
                }
            }
            is OnboardingEvent.UpdateProfilePicture -> {
                _uiState.update {
                    it.copy(
                        userProfile = it.userProfile.copy(
                            profilePictureUri = event.uri
                        )
                    )
                }
            }
            is OnboardingEvent.UpdateTargetLanguage -> {
                _uiState.update {
                    it.copy(
                        userProfile = it.userProfile.copy(
                            targetLanguage = event.language
                        )
                    )
                }
            }
        }
    }
}