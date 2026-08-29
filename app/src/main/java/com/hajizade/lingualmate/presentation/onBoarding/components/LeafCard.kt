package com.hajizade.lingualmate.presentation.onBoarding.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hajizade.lingualmate.ui.theme.*

@Composable
fun LeafCard(
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = LeafShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer // رنگ پس‌زمینه کارت متناسب با تم جدید
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimaryContainer, // رنگ متن هماهنگ با پس‌زمینه
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}