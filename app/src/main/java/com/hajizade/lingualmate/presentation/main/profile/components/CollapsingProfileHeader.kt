package com.hajizade.lingualmate.presentation.main.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingProfileHeader(
    name: String,
    lastSeenText: String,
    imageUrl: String?,
    scrollOffset: Float,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isScrollingUp = scrollOffset > 0f
    val scrollUpProgress = (scrollOffset / 100f).coerceIn(0f, 1f)

    val isPullingDown = scrollOffset < 0f
    val pullDownProgress = (-scrollOffset / 300f).coerceIn(0f, 1f)

    // ۱. تنظیم ارتفاع هدر
    val headerHeight = when {
        isPullingDown -> (220 + (pullDownProgress * 180)).dp
        else -> (220 - (scrollUpProgress * 150)).dp
    }

    // ۲. صفر شدن شعاع گوشه‌ها جهت ایجاد مستطیل کامل
    val cornerRadius = if (isPullingDown) {
        ((1f - pullDownProgress) * 50).dp
    } else {
        50.dp
    }

    // ۳. ارتفاع عکس در حالت کشش
    val imageHeight = if (isPullingDown) (110 + (pullDownProgress * 290)).dp else 110.dp

    val imageAlpha = if (isScrollingUp) (1f - scrollUpProgress * 2f).coerceIn(0f, 1f) else 1f
    val uiElementsAlpha = when {
        pullDownProgress > 0.8f -> ((1f - pullDownProgress) / 0.2f).coerceIn(0f, 1f)
        else -> 1f
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(headerHeight)
    ) {
        // --- عکس پروفایل ---
        Box(
            modifier = Modifier
                .align(if (isPullingDown) Alignment.Center else Alignment.TopCenter)
                .padding(top = if (isPullingDown) 0.dp else 16.dp)
                .then(
                    if (isPullingDown) {
                        // در صورت Pull Down، عرض تصویر برابر کل صفحه می‌شود
                        Modifier
                            .fillMaxWidth()
                            .height(imageHeight)
                    } else {
                        Modifier.size(110.dp)
                    }
                )
                .graphicsLayer { alpha = imageAlpha }
                .clip(RoundedCornerShape(cornerRadius))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            if (!imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = name.firstOrNull()?.uppercase() ?: "Z",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // --- متن‌ها روی عکس ---
        Column(
            modifier = Modifier
                .align(
                    when {
                        isScrollingUp -> Alignment.CenterStart
                        isPullingDown -> Alignment.BottomStart
                        else -> Alignment.BottomCenter
                    }
                )
                .padding(
                    start = if (isScrollingUp || isPullingDown) 20.dp else 0.dp,
                    bottom = if (isPullingDown) 20.dp else 0.dp
                )
                .graphicsLayer { alpha = uiElementsAlpha },
            horizontalAlignment = if (isScrollingUp || isPullingDown) Alignment.Start else Alignment.CenterHorizontally
        ) {
            Text(
                text = name,
                style = if (isScrollingUp) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (isPullingDown) Color.White else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = lastSeenText,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isPullingDown) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // --- آیکون سه نقطه ---
        CompositionLocalProvider(LocalRippleConfiguration provides null) {
            IconButton(
                onClick = onMoreClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .graphicsLayer { alpha = uiElementsAlpha }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = if (isPullingDown) Color.White else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}