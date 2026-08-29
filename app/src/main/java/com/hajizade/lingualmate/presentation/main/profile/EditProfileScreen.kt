package com.hajizade.lingualmate.presentation.main.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.hajizade.lingualmate.data.local.entity.UserProfile
import com.hajizade.lingualmate.presentation.main.profile.components.LanguageDropdown
import com.hajizade.lingualmate.presentation.main.profile.components.ProfileImagePicker

@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    onLogoutSuccess: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    val profile = uiState.userProfile

    if (profile == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    EditProfileContent(
        profile = profile,
        onNavigateBack = onNavigateBack,
        onSaveProfile = {
            viewModel.onEvent(ProfileEvent.SaveProfile)
            onNavigateBack()
        },
        onLogoutClick = {
            // خروج از فایربیس
            FirebaseAuth.getInstance().signOut()
            // هدایت به صفحه لاگین
            onLogoutSuccess()
        },
        onNameChange = { viewModel.onEvent(ProfileEvent.UpdateName(it)) },
        onNativeLangChange = { viewModel.onEvent(ProfileEvent.UpdateNativeLanguage(it)) },
        onTargetLangChange = { viewModel.onEvent(ProfileEvent.UpdateTargetLanguage(it)) },
        onKnownLangsChange = { viewModel.onEvent(ProfileEvent.UpdateKnownLanguages(it)) },
        onBioChange = { viewModel.onEvent(ProfileEvent.UpdateBio(it)) },
        onInterestsChange = { viewModel.onEvent(ProfileEvent.UpdateInterests(it)) },
        onImageSelected = { viewModel.onEvent(ProfileEvent.UpdateProfilePicture(it)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    profile: UserProfile,
    onNavigateBack: () -> Unit,
    onSaveProfile: () -> Unit,
    onLogoutClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onNativeLangChange: (String) -> Unit,
    onTargetLangChange: (String) -> Unit,
    onKnownLangsChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onInterestsChange: (String) -> Unit,
    onImageSelected: (String) -> Unit
) {
    val availableLanguages = remember {
        listOf("English", "Italian", "Persian", "Spanish", "French", "German")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSaveProfile) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save Profile",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // ۱. تصویر پروفایل
            ProfileImagePicker(
                profilePictureUri = profile.profilePictureUri,
                onImageSelected = onImageSelected
            )

            // ۲. نام
            OutlinedTextField(
                value = profile.name,
                onValueChange = onNameChange,
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // ۳. زبان مادری
            LanguageDropdown(
                label = "Native Language",
                selectedLanguage = profile.nativeLanguage,
                languages = availableLanguages,
                onLanguageSelected = onNativeLangChange
            )

            // ۴. زبان هدف
            LanguageDropdown(
                label = "Target Language",
                selectedLanguage = profile.targetLanguage,
                languages = availableLanguages,
                onLanguageSelected = onTargetLangChange
            )

            // ۵. زبان‌های دیگر
            LanguageDropdown(
                label = "Other Known Languages",
                selectedLanguage = profile.knownLanguages,
                languages = availableLanguages,
                onLanguageSelected = onKnownLangsChange
            )

            // ۶. بیوگرافی
            OutlinedTextField(
                value = profile.bio,
                onValueChange = onBioChange,
                label = { Text("Bio") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
            )

            // ۷. علاقه‌مندی‌ها
            OutlinedTextField(
                value = profile.interests,
                onValueChange = onInterestsChange,
                label = { Text("Interests") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ۸. دکمه خروج از حساب کاربری (Log Out)
            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Log Out",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}