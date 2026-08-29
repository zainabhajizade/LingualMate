package com.hajizade.lingualmate.presentation.main.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hajizade.lingualmate.data.local.entity.UserCommentEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CommentItem(
    commentItem: UserCommentEntity,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBackgroundGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceContainerLowest,
            MaterialTheme.colorScheme.surfaceContainerLow
        )
    )

    val avatarRingGradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
        )
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { // 👈 کلیک‌پذیری کامنت برای رفتن به پروفایل
                onUserClick(commentItem.authorId)
            },
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent,
        shadowElevation = 2.dp,
        tonalElevation = 1.dp
    ) {
        Box(
            modifier = Modifier
                .background(cardBackgroundGradient)
                .padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.FormatQuote,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                modifier = Modifier
                    .size(70.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 12.dp, y = 12.dp)
            )

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(avatarRingGradient)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!commentItem.authorAvatarUrl.isNullOrEmpty()) {
                            AsyncImage(
                                model = commentItem.authorAvatarUrl,
                                contentDescription = commentItem.authorName,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(
                                text = commentItem.authorName.take(1).uppercase(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = commentItem.authorName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = formatMillisToDate(commentItem.date),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = commentItem.comment,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

private fun formatMillisToDate(millisString: String?): String {
    val millis = millisString?.toLongOrNull() ?: return millisString ?: ""
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(Date(millis))
}