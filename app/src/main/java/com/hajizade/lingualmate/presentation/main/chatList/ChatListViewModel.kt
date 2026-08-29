package com.hajizade.lingualmate.presentation.main.chatList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hajizade.lingualmate.domain.repository.ChatMessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatMessageRepository: ChatMessageRepository
) : ViewModel(){
    private val _state = MutableStateFlow(ChatListState())
    val state: StateFlow<ChatListState> = _state.asStateFlow()

    init {
        loadChatS()
    }

    private fun loadChatS() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            try {
                chatMessageRepository.getChatPreviews().collect { chats ->
                    _state.update {
                        it.copy(
                            chats = chats,
                            isLoading = false
                        )
                    }
                }
            }catch(e:Exception){
                _state.update {
                    it.copy(
                        error = e.localizedMessage ?: "unknown error",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onEvent(event: ChatListEvent){
        when(event){
            is ChatListEvent.OnChatClicked -> {

            }
            is ChatListEvent.OnDeleteChatClicked -> {
                viewModelScope.launch {
                    deleteChat(event.contactId)
                }
            }
            is ChatListEvent.OnSearchQueryChanged -> {
                _state.update {
                    it.copy(
                        searchQuery = event.query
                    )
                }
            }
        }
    }

    private suspend fun deleteChat(contactId: String) {
        chatMessageRepository.deleteChatWithContact(contactId)
    }

}