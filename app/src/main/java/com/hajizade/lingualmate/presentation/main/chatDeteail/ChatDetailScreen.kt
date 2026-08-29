package com.hajizade.lingualmate.presentation.main.chatDeteail

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hajizade.lingualmate.data.local.entity.ChatMessageEntity
import com.hajizade.lingualmate.data.local.entity.MessageType
import com.hajizade.lingualmate.presentation.main.chatDeteail.components.ChatTopBar
import com.hajizade.lingualmate.presentation.main.chatDeteail.components.MessageBubble
import com.hajizade.lingualmate.presentation.main.chatDeteail.components.MessageInputBar
import com.hajizade.lingualmate.presentation.main.chatDeteail.components.MessageOptionsMenu
import kotlinx.coroutines.launch

@Composable
fun ChatDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToUserProfile: (String) -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ChatDetailContent(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                is ChatUiEvent.OnNavigateBack -> {
                    onNavigateBack()
                }
                is ChatUiEvent.OnUserProfileClick -> {
                    onNavigateToUserProfile(event.userId)
                }
                else -> {
                    viewModel.onEvent(event)
                }
            }
        }
    )
}

@Composable
fun ChatDetailContent(
    uiState: ChatUiState,
    onEvent: (ChatUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    var selectedMessageForMenu by remember { mutableStateOf<ChatMessageEntity?>(null) }

    // نگهداری نوع رسانه‌ای که کاربر قصد ارسال آن را دارد
    var pendingMediaType by remember { mutableStateOf("image") }

    // لانچر برای انتخاب فایل از گالری، موزیک یا فایل‌منجر به همراه Log برای بررسی صحت عملکرد
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            Log.d("GalleryDebug", "Selected URI: $it, Type: $pendingMediaType")
            onEvent(ChatUiEvent.OnSendMedia(it, pendingMediaType))
        } ?: Log.d("GalleryDebug", "URI is null (User canceled selection)")
    }

    // لانچر برای گرفتن مجوز ضبط صدا
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onEvent(ChatUiEvent.OnStartRecordVoice)
        } else {
            Log.d("VoiceDebug", "Record audio permission denied")
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            ChatTopBar(
                user = uiState.receiverUser,
                onNavigateBack = { onEvent(ChatUiEvent.OnNavigateBack) },
                onCallClick = { onEvent(ChatUiEvent.OnCallClick) },
                onVideoCallClick = { onEvent(ChatUiEvent.OnVideoCallClick) },
                onBlockUserClick = { onEvent(ChatUiEvent.OnBlockUserClick) },
                onClearChatClick = { deleteForEveryone ->
                    onEvent(ChatUiEvent.OnClearChatClick(deleteForEveryone))
                },
                onProfileClick = {
                    uiState.receiverUser?.id?.let { userId ->
                        onEvent(ChatUiEvent.OnUserProfileClick(userId))
                    }
                }
            )
        },
        bottomBar = {
            MessageInputBar(
                isBlocked = uiState.receiverUser?.isBlocked ?: false,
                inputText = uiState.inputText,
                isRecording = uiState.isRecording, // 👈 Passed the isRecording state here
                onTextChanged = { newText ->
                    onEvent(ChatUiEvent.OnInputTextChanged(newText))
                },
                onSendMessage = { onEvent(ChatUiEvent.OnSendMessage) },
                onSendMedia = { messageType ->
                    // تشخیص نوع رسانه و تنظیم نوع فیلتر برای لانچر
                    when (messageType) {
                        MessageType.IMAGE -> {
                            pendingMediaType = "image"
                            galleryLauncher.launch("image/*")
                        }
                        MessageType.VIDEO -> {
                            pendingMediaType = "video"
                            galleryLauncher.launch("video/*")
                        }
                        MessageType.AUDIO_FILE -> {
                            pendingMediaType = "audio"
                            galleryLauncher.launch("audio/*")
                        }
                        else -> {
                            pendingMediaType = "document"
                            galleryLauncher.launch("*/*")
                        }
                    }
                },
                onStartRecordVoice = {
                    // Check runtime permission before starting record
                    val permissionCheck = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    )
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        onEvent(ChatUiEvent.OnStartRecordVoice)
                    } else {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                },
                onStopRecordVoice = { onEvent(ChatUiEvent.OnStopRecordVoice) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(uiState.messages, key = { it.id }) { message ->
                    MessageBubble(
                        message = message,
                        senderName = if (!message.isFromMe) uiState.receiverUser?.name else null,
                        senderAvatarUrl = if (!message.isFromMe) uiState.receiverUser?.profilePictureUri else null,
                        onLongClick = {
                            selectedMessageForMenu = message
                        }
                    )
                }
            }

            selectedMessageForMenu?.let { message ->
                MessageOptionsMenu(
                    isFromMe = message.isFromMe,
                    onDismiss = { selectedMessageForMenu = null },
                    onReactionSelect = { reaction ->
                        onEvent(ChatUiEvent.OnReactionSelect(message, reaction))
                        selectedMessageForMenu = null
                    },
                    onCopy = {
                        message.text?.let { text ->
                            coroutineScope.launch {
                                clipboard.setClipEntry(
                                    ClipEntry(
                                        android.content.ClipData.newPlainText("message", text)
                                    )
                                )
                            }
                        }
                        selectedMessageForMenu = null
                    },
                    onTranslate = {
                        onEvent(ChatUiEvent.OnTranslateMessage(message))
                        selectedMessageForMenu = null
                    },
                    onEdit = {
                        onEvent(ChatUiEvent.OnStartEditMessage(message))
                        selectedMessageForMenu = null
                    },
                    onDelete = {
                        onEvent(ChatUiEvent.OnDeleteMessage(message))
                        selectedMessageForMenu = null
                    }
                )
            }
        }
    }
}