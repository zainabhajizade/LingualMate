package com.hajizade.lingualmate.presentation.onBoarding.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hajizade.lingualmate.data.local.entity.toDbString
import com.hajizade.lingualmate.presentation.onBoarding.OnboardingEvent
import com.hajizade.lingualmate.presentation.onBoarding.OnboardingUiState
import com.hajizade.lingualmate.ui.theme.*

@Composable
fun QuestionStepContent(
    questionIndex: Int,
    uiState: OnboardingUiState,
    onEvent: (OnboardingEvent) -> Unit
) {
    // تعداد کل سوالات حالا ۷ تا است (از ۰ تا ۶)
    val totalQuestions = 7

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (questionIndex) {
            // ۰. نام کاربر
            0 -> {
                OnboardingInputField(
                    title = "What is your name?",
                    value = uiState.userProfile.name,
                    placeholder = "Enter your name...",
                    onValueChange = { onEvent(OnboardingEvent.UpdateName(it)) }
                )
            }

            // ۱. ایمیل
            1 -> {
                OnboardingInputField(
                    title = "What is your email address?",
                    value = uiState.userProfile.email,
                    placeholder = "e.g. user@example.com",
                    onValueChange = { onEvent(OnboardingEvent.UpdateEmail(it)) }
                )
            }

            // ۲. زبان مادری (بدون سطح تسلط - فقط انتخاب یک زبان)
            2 -> {
                Text(
                    text = "What is your native language?",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
                LanguageDropdown(
                    selectedLanguage = uiState.userProfile.nativeLanguage,
                    onLanguageSelected = { onEvent(OnboardingEvent.UpdateNativeLanguage(it)) }
                )
            }

            // ۳. زبان‌های مورد یادگیری + سطح (Target Languages)
            3 -> {
                Text(
                    text = "Which languages do you want to learn?",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))

                // لیست فعلی را خوانده و تبدیل به List<SelectedLanguage> می‌کنیم
                val currentTargetList = uiState.userProfile.getTargetLanguagesList()

                LanguageWithLevelSelector(
                    selectedLanguages = currentTargetList,
                    onAddLanguage = { newLang ->
                        val updatedList = currentTargetList + newLang
                        onEvent(OnboardingEvent.UpdateTargetLanguage(updatedList.toDbString()))
                    },
                    onRemoveLanguage = { langToRemove ->
                        val updatedList = currentTargetList - langToRemove
                        onEvent(OnboardingEvent.UpdateTargetLanguage(updatedList.toDbString()))
                    }
                )
            }

            // ۴. زبان‌های دیگری که می‌داند + سطح (Known Languages)
            4 -> {
                Text(
                    text = "Other languages you speak?",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))

                val currentKnownList = uiState.userProfile.getKnownLanguagesList()

                LanguageWithLevelSelector(
                    selectedLanguages = currentKnownList,
                    onAddLanguage = { newLang ->
                        val updatedList = currentKnownList + newLang
                        onEvent(OnboardingEvent.UpdateKnownLanguages(updatedList.toDbString()))
                    },
                    onRemoveLanguage = { langToRemove ->
                        val updatedList = currentKnownList - langToRemove
                        onEvent(OnboardingEvent.UpdateKnownLanguages(updatedList.toDbString()))
                    }
                )
            }

            // ۵. علاقه‌مندی‌ها (Interests)
            5 -> {
                OnboardingInputField(
                    title = "What are your interests?",
                    value = uiState.userProfile.interests,
                    placeholder = "e.g. Movies, Tech, Travel...",
                    onValueChange = { onEvent(OnboardingEvent.UpdateInterests(it)) }
                )
            }

            // ۶. بیوگرافی (Bio)
            6 -> {
                OnboardingInputField(
                    title = "Tell us a bit about yourself (Bio)",
                    value = uiState.userProfile.bio,
                    placeholder = "Write a short intro...",
                    singleLine = false,
                    onValueChange = { onEvent(OnboardingEvent.UpdateBio(it)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- بررسی معتبر بودن مرحله فعلی ---
        val isLastQuestion = questionIndex == totalQuestions - 1
        val isNextEnabled = when (questionIndex) {
            0 -> uiState.userProfile.name.isNotBlank()
            1 -> uiState.userProfile.email.isNotBlank()
            2 -> uiState.userProfile.nativeLanguage.isNotBlank()
            3 -> uiState.userProfile.targetLanguage.isNotBlank() // حداقل یک زبان هدف باید اضافه شده باشد
            4 -> true // اختیاری
            5 -> true // اختیاری
            6 -> true // اختیاری
            else -> true
        }

        // دکمه‌های ناوبری
        OnboardingNavigationButtons(
            questionIndex = questionIndex,
            isLastQuestion = isLastQuestion,
            isNextEnabled = isNextEnabled,
            onBackClick = { onEvent(OnboardingEvent.PreviousQuestion) },
            onNextClick = {
                if (isLastQuestion) {
                    onEvent(OnboardingEvent.SubmitOnboarding)
                } else {
                    onEvent(OnboardingEvent.NextQuestion)
                }
            }
        )
    }
}

@Composable
private fun OnboardingInputField(
    title: String,
    value: String,
    placeholder: String,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(24.dp))

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        singleLine = singleLine
    )
}