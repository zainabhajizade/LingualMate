package com.hajizade.lingualmate.presentation.main.chatDeteail

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hajizade.lingualmate.data.local.entity.ChatMessageEntity
import com.hajizade.lingualmate.data.local.entity.MessageStatus
import com.hajizade.lingualmate.data.local.entity.MessageType
import com.hajizade.lingualmate.domain.repository.ChatMessageRepository
import com.hajizade.lingualmate.domain.repository.UserProfileRepository
import com.hajizade.lingualmate.util.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val audioRecorder: AudioRecorder,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: String = checkNotNull(savedStateHandle["userId"])

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        observeChatData()
    }

    private fun observeChatData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // گرفتن شناسه کاربر جاری برای پروفایل خودمان
            val currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""

            combine(
                chatMessageRepository.getMessages(userId),
                userProfileRepository.getUserProfileById(userId), // 👈 پروفایل کاربر مقابل (Target User)
                userProfileRepository.getUserProfile(currentUserId) // 👈 پروفایل خودِ شما (My Profile با ورودی شناسه خودش)
            ) { messages, receiverProfile, myProfile ->
                _uiState.value.copy(
                    isBlocked = myProfile?.isBlocked ?: false,
                    messages = messages,
                    receiverUser = receiverProfile, // 👈 نام و پروفایل کاربر مقابل در هدر چت نمایش داده می‌شود
                    isLoading = false
                )
            }.catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun onEvent(event: ChatUiEvent) {
        when (event) {
            is ChatUiEvent.OnInputTextChanged -> {
                _uiState.update { it.copy(inputText = event.newText) }
            }
            is ChatUiEvent.OnSendMessage -> {
                sendMessage()
            }
            is ChatUiEvent.OnSendMedia -> {
                sendMediaMessage(event.uri, event.fileType)
            }

            // 👇 اینجا رو برای شروع و پایان ضبط صدا اضافه کن
            is ChatUiEvent.OnStartRecordVoice -> {
                startRecordingVoice()
            }
            is ChatUiEvent.OnStopRecordVoice -> {
                stopAndSendRecordedVoice()
            }
            // ------------------------------------------

            is ChatUiEvent.OnStartEditMessage -> {
                _uiState.update {
                    it.copy(
                        inputText = event.message.text ?: "",
                        editingMessageId = event.message.id
                    )
                }
            }
            is ChatUiEvent.OnCancelEdit -> {
                _uiState.update { it.copy(inputText = "", editingMessageId = null) }
            }
            is ChatUiEvent.OnDeleteMessage -> {
                deleteMessage(event.message)
            }
            is ChatUiEvent.OnTranslateMessage -> {
                translateMessage(event.message)
            }
            is ChatUiEvent.OnReactionSelect -> {
                toggleReaction(event.message.id, event.reaction)
            }
            is ChatUiEvent.OnClearChatClick -> {
                clearChat()
            }
            is ChatUiEvent.OnBlockUserClick -> {
                val currentStatus = uiState.value.receiverUser?.isBlocked ?: false
                toggleBlockUser(!currentStatus)
            }
            else -> {
                // سایر ایونت‌ها
            }
        }
    }

    private fun sendMessage() {
        val text = uiState.value.inputText.trim()
        if (text.isEmpty()) return

        val editingId = uiState.value.editingMessageId

        viewModelScope.launch {
            if (editingId != null) {
                chatMessageRepository.editMessage(editingId, text)
                _uiState.update { it.copy(inputText = "", editingMessageId = null) }
            } else {
                val myCurrentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""

                val newMessage = ChatMessageEntity(
                    id = UUID.randomUUID().toString(),
                    senderId = myCurrentUserId,
                    receiverId = userId,
                    text = text,
                    timestamp = System.currentTimeMillis(),
                    mediaUrl = null,
                    fileName = null,
                    fileSize = null,
                    duration = null,
                    isFromMe = true,
                    reactions = "",
                    type = MessageType.TEXT,
                    status = MessageStatus.SENT,
                    isEdited = false,
                    isDeletedForMe = false,
                    isDeletedForEveryone = false,
                    isTranslated = false,
                    translatedText = null
                )
                chatMessageRepository.sendMessage(newMessage)
                _uiState.update { it.copy(inputText = "") }
            }
        }
    }

    // 👈 تابع جدید برای هندل کردن آپلود و ارسال فایل/گالری/موزیک از طریق Repository که از قبل نوشته بودید
    private fun sendMediaMessage(fileUri: Uri, fileType: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) } // اختیاری: برای نشان دادن وضعیت بارگذاری

            val success = chatMessageRepository.sendFileMessage(
                receiverId = userId,
                fileUri = fileUri,
                fileType = fileType
            )

            _uiState.update { it.copy(isLoading = false) }

            if (!success) {
                _uiState.update { it.copy(error = "failed to send files") }
            }
        }
    }

    private fun toggleReaction(messageId: String, reaction: String) {
        viewModelScope.launch {
            chatMessageRepository.toggleReaction(messageId, reaction)
        }
    }

    private fun translateMessage(message: ChatMessageEntity) {
        val originalText = message.text ?: return
        viewModelScope.launch {
            val dummyTranslation = "Translated: $originalText"
            chatMessageRepository.saveTranslation(message.id, dummyTranslation)
        }
    }

    fun deleteMessage(message: ChatMessageEntity) {
        viewModelScope.launch {
            // مطمئن شویم کاربر فقط پیام خودش را پاک می‌کند
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
            if (message.senderId == currentUserId) {
                chatMessageRepository.deleteMessage(message)
            }
        }
    }

    private fun clearChat() {
        viewModelScope.launch {
            chatMessageRepository.clearChat(userId)
        }
    }

    private fun toggleBlockUser(isBlocked: Boolean) {
        viewModelScope.launch {
            userProfileRepository.toggleBlockUser(userId, isBlocked)
        }
    }
    private fun startRecordingVoice() {
        // شروع ضبط صدا
        audioRecorder.startRecording()

        // آپدیت کردن UI (اگر فیلدی مثل isRecording در UiState داری)
        _uiState.update { it.copy(isRecording = true) }
    }

    private fun stopAndSendRecordedVoice() {
        // ۱. متوقف کردن ضبط و تحویل گرفتن فایل صوتی
        val audioFile = audioRecorder.stopRecording()

        // تغییر وضعیت UI به حالت عادی
        _uiState.update { it.copy(isRecording = false) }

        // ۲. اگر فایل با موفقیت ایجاد شده بود، آن را تبدیل به Uri کرده و ارسال کن
        audioFile?.let { file ->
            val fileUri = Uri.fromFile(file)
            sendMediaMessage(fileUri, "audio")
        }
    }
}