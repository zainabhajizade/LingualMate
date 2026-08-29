package com.hajizade.lingualmate.presentation.main.chatDeteail.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hajizade.lingualmate.data.local.entity.ChatMessageEntity
import com.hajizade.lingualmate.data.local.entity.MessageStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageBubble(
    message: ChatMessageEntity,
    senderName: String? = null,
    senderAvatarUrl: String? = null,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFromMe = message.isFromMe

    // رنگ‌های دیزاین اول (بر پایه تم برنامه)
    val bubbleColor = if (isFromMe) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = if (isFromMe) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    // شکل حباب تلگرامی ناهمسان
    val bubbleShape = if (isFromMe) {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 4.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 4.dp,
            bottomEnd = 16.dp
        )
    }

    val formattedTime = remember(message.timestamp) {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.format(Date(message.timestamp))
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 3.dp),
        horizontalArrangement = if (isFromMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        // آواتار مخاطب (سمت چپ)
        if (!isFromMe) {
            Surface(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            ) {
                if (!senderAvatarUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = senderAvatarUrl,
                        contentDescription = senderName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
        }

        // بدنه اصلی پیام
        Column(horizontalAlignment = if (isFromMe) Alignment.End else Alignment.Start) {
            Surface(
                shape = bubbleShape,
                color = bubbleColor,
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .combinedClickable(
                        onClick = { },
                        onLongClick = onLongClick
                    )
            ) {
                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                    // نام مخاطب بالای پیام
                    if (!isFromMe && !senderName.isNullOrEmpty()) {
                        Text(
                            text = senderName,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }

                    // متن پیام
                    if (!message.text.isNullOrEmpty()) {
                        Text(
                            text = message.text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor
                        )
                    }
                    if (!message.translatedText.isNullOrEmpty()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = textColor.copy(alpha = 0.15f)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🌐 ${message.translatedText}",
                                style = MaterialTheme.typography.bodySmall,
                                color = textColor.copy(alpha = 0.8f)
                            )
                        }
                    }

                    // ساعت و وضعیت ارسال
                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formattedTime,
                            fontSize = 10.sp,
                            color = textColor.copy(alpha = 0.6f)
                        )

                        if (isFromMe) {
                            Spacer(modifier = Modifier.width(3.dp))
                            val isRead = message.status == MessageStatus.READ
                            Icon(
                                imageVector = if (isRead) Icons.Default.DoneAll else Icons.Default.Done,
                                contentDescription = "Status",
                                tint = if (isRead) Color(0xFF2196F3) else textColor.copy(alpha = 0.6f),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }

            // چیپ ری‌اکشن پایین حباب
            if (message.reactions.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .offset(y = (-6).dp, x = if (isFromMe) (-6).dp else 6.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = message.reactions, fontSize = 12.sp)
                }
            }
        }
    }
}

