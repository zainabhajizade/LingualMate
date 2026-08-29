package com.hajizade.lingualmate.presentation.main.community

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hajizade.lingualmate.data.local.entity.UserProfile
import com.hajizade.lingualmate.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val profileRepository: UserProfileRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel(){


    private val _State = MutableStateFlow(CommunityUiState(isLoading = true))
    val State: StateFlow<CommunityUiState> = _State.asStateFlow()

    init {
        loadUsers()

    }
    private fun loadUsers() {
        viewModelScope.launch {
            try {
                val userList = profileRepository.getAllUsers()

                // 👈 محاسبه تعداد کامنت برای هر کاربر
                val usersWithCommentsCount = userList.map { user ->
                    try {
                        // فرض کنید متدی در ریپازیتوری برای گرفتن کامنت‌های هر کاربر وجود دارد
                        val comments = profileRepository.getCommentsForUser(user.id)
                        user.copy(commentsCount = comments.size)
                    } catch (e: Exception) {
                        user // اگر خطایی رخ داد، همان کاربر بدون تغییر بماند
                    }
                }

                _State.update {
                    it.copy(
                        users = usersWithCommentsCount,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _State.update {
                    it.copy(
                        isLoading = false,
                        error = e.localizedMessage ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun onEvent(event: CommunityEvent){
        when(event){
            is CommunityEvent.OnLanguageFilterSelected -> {
                _State.update { it.copy(selectedLanguageFilter = event.language) }
            }
            is CommunityEvent.OnSearchQueryChanged -> {
                _State.update { it.copy(searchQuery = event.query) }
            }
            is CommunityEvent.OnUserClicked -> {
                // مدیریت رویداد کلیک روی کاربر در صورت نیاز
            }
        }
    }
}