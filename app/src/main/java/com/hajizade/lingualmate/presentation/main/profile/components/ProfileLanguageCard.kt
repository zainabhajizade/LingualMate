package com.hajizade.lingualmate.presentation.main.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class LanguageItem(
    val name: String,
    val flagEmoji: String,
    val level: String? = null,
    val progress: Float? = null // عدد بین 0.0 تا 1.0
)

@Composable
fun ProfileLanguageCard(
    nativeLanguages: List<LanguageItem>,
    targetLanguage: LanguageItem,
    modifier: Modifier = Modifier
) {
    // گرادینت نرم برای کل کارت
    val mainCardGradient = Brush.verticalGradient(
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
                .background(mainCardGradient)
                .padding(22.dp)
        ) {
            // --- ۱. هدر و کارت هیجان‌انگیز زبان هدف ---
            SectionHeader(title = "TARGET LANGUAGE", icon = Icons.Rounded.AutoAwesome)

            Spacer(modifier = Modifier.height(14.dp))

            TargetLanguageHeroBlock(targetLanguage = targetLanguage)

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- ۲. بخش زبان‌های مادری و مسلط ---
            SectionHeader(title = "NATIVE & KNOWN", icon = Icons.Rounded.Translate)

            Spacer(modifier = Modifier.height(14.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                nativeLanguages.forEach { lang ->
                    KnownLanguageRow(language = lang)
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(14.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.2.sp
        )
    }
}

// کارت هیرو برای زبان هدف با افکت گرادینت و درصد پیشرفت
@Composable
private fun TargetLanguageHeroBlock(targetLanguage: LanguageItem) {
    val heroGradient = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
            MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.25f)
        )
    )

    Surface(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .background(heroGradient)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // آیکون پرچم با هاله انیمیشنی
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = targetLanguage.flagEmoji, fontSize = 24.sp)
                    }

                    Column {
                        Text(
                            text = targetLanguage.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        targetLanguage.level?.let { level ->
                            Text(
                                text = level,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // درصد پیشرفت عددی (Percentage Badge)
                targetLanguage.progress?.let { p ->
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = "${(p * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            // نوار پیشرفت درصد یادگیری
            targetLanguage.progress?.let { progressValue ->
                Spacer(modifier = Modifier.height(14.dp))
                LinearProgressIndicator(
                    progress = { progressValue },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// ردیف زبان‌های بلدید با استایل مینیمال و شیک
@Composable
private fun KnownLanguageRow(language: LanguageItem) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = language.flagEmoji, fontSize = 20.sp)
                }

                Text(
                    text = language.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            language.level?.let { levelText ->
                Surface(
                    shape = CircleShape,
                    color = if (levelText.lowercase() == "native") {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHighest
                    }
                ) {
                    Text(
                        text = levelText,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (levelText.lowercase() == "native") {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileLanguageCardPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        ProfileLanguageCard(
            targetLanguage = LanguageItem(
                name = "Italian",
                flagEmoji = "🇮🇹",
                level = "Intermediate (B2)",
                progress = 0.65f
            ),
            nativeLanguages = listOf(
                LanguageItem("Persian", "🇮🇷", "Native"),
                LanguageItem("English", "🇬🇧", "Advanced (C1)")
            )
        )
    }
}