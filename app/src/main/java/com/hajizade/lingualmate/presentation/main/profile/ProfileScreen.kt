package com.hajizade.lingualmate.presentation.main.profile

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hajizade.lingualmate.presentation.main.profile.components.*

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onEditProfile: () -> Unit,
    onNavigationToUser :(String) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    val profile = uiState.userProfile

    // مدیریت وضعیت نمایش BottomSheet
    var showBottomSheet by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    var pullDownOffset by remember { mutableFloatStateOf(0f) }

    // ۱. انیمیشن اسکرول هدر
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0) {
                    if (available.y > 0) {
                        pullDownOffset = (pullDownOffset - available.y * 0.2f).coerceAtLeast(-300f)
                        return Offset(0f, available.y)
                    } else if (available.y < 0 && pullDownOffset < 0f) {
                        pullDownOffset = (pullDownOffset - available.y * 0.3f).coerceAtMost(0f)
                        return Offset(0f, available.y)
                    }
                } else {
                    pullDownOffset = 0f
                }
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                return super.onPostFling(consumed, available)
            }
        }
    }

    val rawScrollOffset by remember {
        derivedStateOf {
            if (pullDownOffset < 0f) {
                pullDownOffset
            } else if (listState.firstVisibleItemIndex == 0) {
                (listState.firstVisibleItemScrollOffset.toFloat() / 35f).coerceIn(0f, 100f)
            } else {
                100f
            }
        }
    }

    val animatedScrollOffset by animateFloatAsState(
        targetValue = rawScrollOffset,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessVeryLow
        ),
        label = "smoothScrollOffset"
    )

    // ۲. نمایش لودینگ تا زمان دریافت داده‌ها
    if (uiState.isLoading || profile == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // ۳. آماده‌سازی داده‌های زبان‌ها
    val targetLanguagesList = profile.getTargetLanguagesList()
    val firstTarget = targetLanguagesList.firstOrNull()

    val targetLanguageItem = LanguageItem(
        name = firstTarget?.languageName ?: "",
        flagEmoji = "🌐",
        level = firstTarget?.level?.name ?: "",
        progress = 0.5f
    )

    val nativeLanguagesList = listOf(
        LanguageItem(
            name = profile.nativeLanguage,
            flagEmoji = "🌐",
            level = "Native"
        )
    )

    // ۴. ساختار اصلی UI
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                CollapsingProfileHeader(
                    name = profile.name,
                    lastSeenText = "Online",
                    imageUrl = profile.profilePictureUri,
                    scrollOffset = animatedScrollOffset,
                    onMoreClick = {
                        viewModel.onEvent(ProfileEvent.OnMoreClick)
                        showBottomSheet = true
                    }
                )
            }

            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    ProfileLanguageCard(
                        nativeLanguages = nativeLanguagesList,
                        targetLanguage = targetLanguageItem
                    )
                }
            }

            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    ProfileBioInterestsCard(
                        bio = profile.bio,
                        interests = profile.interests
                    )
                }
            }

            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    ProfileReviewsCard(
                        comments = uiState.comments,
                        newCommentText = "", // چون پروفایل خود شخص است، نیازی به نوشتن کامنت نیست
                        onCommentChanged = {},
                        onSubmitClick = {},
                        onUserClick = {userId ->
                            onNavigationToUser(userId)

                        }
                    )
                }
            }
        }

        // ۵. کامپوننت جداگانه BottomSheet
        if (showBottomSheet) {
            ProfileOptionsBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                onEditProfileClick = onEditProfile,
                onSettingsClick = {
                    // اکشن‌های مربوط به تنظیمات
                }
            )
        }
    }
}