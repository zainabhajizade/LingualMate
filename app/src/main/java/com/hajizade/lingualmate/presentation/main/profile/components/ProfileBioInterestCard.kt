package com.hajizade.lingualmate.presentation.main.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileBioInterestsCard(
    bio: String,
    interests: String,
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
            // --- ۱. بخش بیوگرافی (About Me) ---
            SectionHeader(title = "ABOUT ME", icon = Icons.Rounded.FormatQuote)

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = bio.ifEmpty { "No bio added yet." },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- ۲. بخش علاقه‌مندی‌ها (Interests - Text Field Style) ---
            SectionHeader(title = "INTERESTS", icon = Icons.Rounded.AutoAwesome)

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = interests,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(16.dp)
                )
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

@Preview(showBackground = true)
@Composable
private fun ProfileBioInterestsCardPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        ProfileBioInterestsCard(
            bio = "Hey there! I love practicing Italian and learning about different cultures. Huge fan of coding, movie nights, and espresso!",
            interests = ("Coding")
        )
    }
}