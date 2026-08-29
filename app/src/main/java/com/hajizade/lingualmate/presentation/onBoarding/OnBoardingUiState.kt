package com.hajizade.lingualmate.presentation.onBoarding


import com.hajizade.lingualmate.data.local.entity.UserProfile

// ۱. مشخص کردن گام فعلی صفحه
sealed interface OnboardingStep {
    // کارت‌های معارفه (شماره کارت از ۰ تا ۲)
    data class Intro(val cardIndex: Int = 0) : OnboardingStep

    // مرحله سوالات (شماره سوال از ۰ تا N)
    data class Question(val questionIndex: Int = 0) : OnboardingStep
}

// ۲. وضعیت کل صفحه Onboarding
data class OnboardingUiState(
    val currentStep: OnboardingStep = OnboardingStep.Intro(cardIndex = 0),
    val userProfile: UserProfile = UserProfile(), // تمام پاسخ‌های کاربر در طول سوالات در این شیء جمع می‌شود
    val isCompleted: Boolean = false // زمانی که تمام سوالات تمام شد true می‌شود
)