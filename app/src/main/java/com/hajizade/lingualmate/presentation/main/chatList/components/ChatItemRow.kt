package com.hajizade.lingualmate.presentation.main.chatList.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hajizade.lingualmate.domain.model.ChatPreviewItem
import com.hajizade.lingualmate.ui.theme.*

@Composable
fun ChatItemRow(
    chat: ChatPreviewItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // عکس پروفایل به همراه نقطه سبز آنلاین بودن
        Box {
            Surface(
                modifier = Modifier.size(54.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = chat.contactName.take(1),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (chat.isOnline) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(Color(0xFF4CAF50), CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // نام، آخرین پیام و زمان
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = chat.contactName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = chat.lastMessage ?: "No messages yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Just now", // یا تبدیل timestamp به زمان نسبی
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }

        // نشانگر تعداد پیام خوانده نشده یا Your Turn
        if (chat.unreadCount > 0) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = "${chat.unreadCount}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }
    }
}