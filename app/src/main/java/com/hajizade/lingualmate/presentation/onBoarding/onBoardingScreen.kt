package com.hajizade.lingualmate.presentation.onBoarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.hajizade.lingualmate.presentation.onBoarding.components.LeafCard
import com.hajizade.lingualmate.presentation.onBoarding.components.QuestionStepContent
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit,
    viewModel: OnBoardingViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val cardTexts = remember {
        listOf(
            "Welcome to LingualMate!\nStart your language learning journey today.",
            "Learn faster with interactive and modern features.",
            "Ready to begin?\nLet's take the first step together!"
        )
    }

    val translateY = remember { Animatable(-1500f) }

    LaunchedEffect(uiState.currentStep) {
        val step = uiState.currentStep
        if (step is OnboardingStep.Intro) {
            // ۱. شروع از بالای صفحه
            translateY.snapTo(-1500f)

            // ۲. ورود سریع تا مرکز صفحه
            translateY.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                )
            )

            // ۳. مکث ۲ ثانیه‌ای
            delay(2.seconds)

            // ۴. خروج سریع به سمت پایین
            translateY.animateTo(
                targetValue = 1500f,
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        val currentStep = uiState.currentStep

        // نمایش کارت‌های Intro
        if (currentStep is OnboardingStep.Intro) {
            val cardIndex = currentStep.cardIndex
            if (cardIndex in cardTexts.indices) {
                LeafCard(
                    text = cardTexts[cardIndex],
                    modifier = Modifier
                        .size(width = 280.dp, height = 200.dp)
                        .graphicsLayer {
                            translationY = translateY.value
                        }
                )
            }
        }

        // نمایش بخش سوالات
        AnimatedVisibility(
            visible = currentStep is OnboardingStep.Question,
            enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight / 2 }) + fadeIn(animationSpec = tween(600))
        ) {
            if (currentStep is OnboardingStep.Question) {
                QuestionStepContent(
                    questionIndex = currentStep.questionIndex,
                    uiState = uiState,
                    onEvent = { event ->
                        viewModel.onEvent(event)

                        // در صورتی که رویداد ثبت نهایی اجرا شد، Navigation به صفحه اصلی منتقل می‌شود
                        if (event is OnboardingEvent.SubmitOnboarding) {
                            onFinishOnboarding()
                        }
                    }
                )
            }
        }
    }
}