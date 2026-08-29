package com.hajizade.lingualmate.presentation.main.chatDeteail.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

// ۱. کامپوننت دکمه ایموجی انیمیشن‌پذیر
@Composable
private fun AnimatedEmojiItem(
    emoji: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.4f else 1.0f,
        animationSpec = spring(dampingRatio = 0.5f),
        label = "emojiScale"
    )

    Text(
        text = emoji,
        fontSize = 26.sp,
        modifier = Modifier
            .scale(scale)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(4.dp)
    )
}

// ۲. دیالوگ اصلی ری‌اکشن‌ها و منوی گزینه‌ها
@Composable
fun MessageOptionsMenu(
    isFromMe: Boolean,
    onDismiss: () -> Unit,
    onReactionSelect: (String) -> Unit,
    onCopy: () -> Unit,
    onTranslate: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var isFullEmojiPickerOpen by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // الف) بخش ایموجی‌ها
                if (!isFullEmojiPickerOpen) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        EmojiData.quickReactions.forEach { reaction ->
                            AnimatedEmojiItem(emoji = reaction.unicode) {
                                onReactionSelect(reaction.unicode)
                                onDismiss()
                            }
                        }

                        IconButton(
                            onClick = { isFullEmojiPickerOpen = true },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "More Emojis",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Select Reaction",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(bottom = 8.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(6),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EmojiData.fullEmojiCategories.values.flatten().forEach { emoji ->
                            item {
                                AnimatedEmojiItem(emoji = emoji) {
                                    onReactionSelect(emoji)
                                    onDismiss()
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // ب) لیست اکشن‌های پیام
                MenuItemRow(icon = Icons.Outlined.ContentCopy, title = "Copy Text") {
                    onCopy()
                    onDismiss()
                }

                MenuItemRow(icon = Icons.Outlined.Translate, title = "Translate") {
                    onTranslate()
                    onDismiss()
                }

                // گزینه Edit فقط برای پیام‌های خود کاربر نشان داده می‌شود
                if (isFromMe) {
                    MenuItemRow(icon = Icons.Outlined.Edit, title = "Edit Message") {
                        onEdit()
                        onDismiss()
                    }
                }

                MenuItemRow(
                    icon = Icons.Outlined.Delete,
                    title = "Delete Message",
                    isDestructive = true
                ) {
                    onDelete()
                    onDismiss()
                }
            }
        }
    }
}

// ۳. کامپوننت کمکی برای رسم هر سطر از گزینه‌های منو
@Composable
private fun MenuItemRow(
    icon: ImageVector,
    title: String,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    val color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )
    }
}