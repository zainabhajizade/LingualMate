package com.hajizade.lingualmate.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hajizade.lingualmate.data.local.entity.UserProfile

@Composable
fun UserCard(
    user: UserProfile,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ۱. عکس پروفایل
            Box {
                Surface(
                    modifier = Modifier.size(68.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (!user.profilePictureUri.isNullOrEmpty()) {
                            AsyncImage(
                                model = user.profilePictureUri,
                                contentDescription = user.name,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(
                                text = user.name.take(1).uppercase(),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // نقطه آنلاین
                if (user.isOnline) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .offset(x = 4.dp, y = (-4).dp)
                            .size(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // ۲. بخش اطلاعات و زبان‌ها
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // نمایش تعداد کامنت‌ها به صورت پویا (فقط اگر بزرگتر از صفر باشد)
                    if (user.commentsCount > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${user.commentsCount}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = "💬", fontSize = 12.sp)
                        }
                    }
                }

                if (user.bio.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = user.bio,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // بخش SPEAKS و LEARNS واقعی
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // SPEAKS
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "SPEAKS ",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                        Text(
                            text = getLanguageFlag(user.nativeLanguage),
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = user.nativeLanguage.ifEmpty { "English" },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        )
                    }

                    // LEARNS
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "LEARNS ",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                        Text(
                            text = getLanguageFlag(user.targetLanguage),
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = user.targetLanguage.ifEmpty { "Italian" },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

fun getLanguageFlag(language: String): String {
    val langName = language.substringBefore(":").lowercase().trim()
    return when (langName) {
        "Spanish", "اسپانیایی" -> "🇪🇸"
        "English", "انگلیسی" -> "🇺🇸"
        "Italian", "ایتالیایی" -> "🇮🇹"
        "German", "آلمانی" -> "🇩🇪"
        "Persian", "فارسی" -> "🇮🇷"
        else -> "🌐"
    }
}