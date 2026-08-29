package com.hajizade.lingualmate.presentation.onBoarding

sealed interface OnboardingEvent {
    // ۱. اکشن‌های مربوط به کارت‌های ۳ ثانیه‌ای (Intro)
    object NextIntroCard : OnboardingEvent

    // ۲. اکشن‌های ثبت پاسخ سوالات (Question Phase)
    data class UpdateName(val name: String) : OnboardingEvent
    data class UpdateEmail(val email: String) : OnboardingEvent
    data class UpdateProfilePicture(val uri: String) : OnboardingEvent
    data class UpdateNativeLanguage(val language: String) : OnboardingEvent
    data class UpdateTargetLanguage(val language: String) : OnboardingEvent
    data class UpdateKnownLanguages(val languages: String) : OnboardingEvent
    data class UpdateInterests(val interests: String) : OnboardingEvent
    data class UpdateBio(val bio: String) : OnboardingEvent

    // ۳. اکشن‌های جابه‌جایی بین سوالات
    object NextQuestion : OnboardingEvent
    object PreviousQuestion : OnboardingEvent
    object SubmitOnboarding : OnboardingEvent
}