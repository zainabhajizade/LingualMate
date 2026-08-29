package com.hajizade.lingualmate.presentation.main.chatDeteail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.hajizade.lingualmate.data.local.entity.MessageType

@Composable
fun MessageInputBar(
    isBlocked: Boolean,
    inputText: String,
    isRecording: Boolean, // Added to track recording state
    onTextChanged: (String) -> Unit,
    onSendMessage: () -> Unit,
    onSendMedia: (MessageType) -> Unit,
    onStartRecordVoice: () -> Unit,
    onStopRecordVoice: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isCameraButtonMode by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    if (isBlocked) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "You blocked this user. Tap to unblock.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { /* Handle Unblock action */ }
            )
        }
    } else {
        Column(modifier = modifier.fillMaxWidth()) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Show recording indicator box when recording, otherwise show standard TextField
                if (isRecording) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.errorContainer),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "🔴 Recording voice...",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    TextField(
                        value = inputText,
                        onValueChange = onTextChanged,
                        placeholder = { Text("Message") },
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ),
                        maxLines = 8,
                        leadingIcon = {
                            IconButton(onClick = { showBottomSheet = true }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add attachment"
                                )
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (isRecording) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary
                        )
                        .pointerInput(inputText, isCameraButtonMode) {
                            detectTapGestures(
                                onTap = {
                                    if (inputText.isNotBlank()) {
                                        onSendMessage()
                                    } else {
                                        isCameraButtonMode = !isCameraButtonMode
                                    }
                                },
                                onPress = {
                                    if (inputText.isBlank() && !isCameraButtonMode) {
                                        onStartRecordVoice()
                                        tryAwaitRelease()
                                        onStopRecordVoice()
                                    }
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    val icon = when {
                        inputText.isNotBlank() -> Icons.AutoMirrored.Filled.Send
                        isCameraButtonMode -> Icons.Default.Videocam
                        else -> Icons.Default.Mic
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = "Action Button",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            if (showBottomSheet) {
                AttachmentBottomSheet(
                    onDismiss = { showBottomSheet = false },
                    onSendMedia = onSendMedia
                )
            }
        }
    }
}