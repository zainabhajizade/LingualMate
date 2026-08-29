package com.hajizade.lingualmate.presentation.main.chatDeteail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hajizade.lingualmate.data.local.entity.MessageType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentBottomSheet(
    onDismiss: () -> Unit,
    onSendMedia: (MessageType) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AttachmentItem(
                icon = Icons.Default.Image,
                label = "Gallery",
                color = Color(0xFFE91E63)
            ) {
                // 💡 ابتدا باتم‌شیت بسته شود تا تمرکز به لانچر سیستم منتقل گردد
                onDismiss()
                onSendMedia(MessageType.IMAGE)
            }

            AttachmentItem(
                icon = Icons.Default.Folder,
                label = "Files",
                color = Color(0xFF2196F3)
            ) {
                onDismiss()
                onSendMedia(MessageType.DOCUMENT)
            }

            AttachmentItem(
                icon = Icons.Default.AudioFile,
                label = "Music",
                color = Color(0xFFFF9800)
            ) {
                onDismiss()
                onSendMedia(MessageType.AUDIO_FILE)
            }
        }
    }
}

@Composable
private fun AttachmentItem(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f))
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp, style = MaterialTheme.typography.labelMedium)
    }
}