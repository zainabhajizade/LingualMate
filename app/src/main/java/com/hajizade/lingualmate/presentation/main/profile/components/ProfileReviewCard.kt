package com.hajizade.lingualmate.presentation.main.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.hajizade.lingualmate.data.local.entity.UserCommentEntity

@Composable
fun ProfileReviewsCard(
    comments: List<UserCommentEntity>,
    newCommentText: String,
    onCommentChanged: (String) -> Unit,
    onUserClick: (String) -> Unit, // 👈 دریافت تابع کلیک
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.surfaceContainerLow
        )
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        tonalElevation = 2.dp,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .background(cardGradient)
                .padding(22.dp)
        ) {
            CommentCardHeader(commentsCount = comments.size)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newCommentText,
                    onValueChange = onCommentChanged,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Write a comment...") },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onSubmitClick,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Send")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (comments.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    comments.forEach { comment ->
                        CommentItem(
                            commentItem = comment,
                            onUserClick = { userId ->
                                onUserClick(userId) // 👈 انتقال آیدی به بالا
                            }
                        )
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No comments yet. Be the first to write one!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}