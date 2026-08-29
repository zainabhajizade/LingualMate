package com.hajizade.lingualmate.presentation.onBoarding.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hajizade.lingualmate.ui.theme.*

@Composable
fun OnboardingNavigationButtons(
    questionIndex: Int,
    isLastQuestion: Boolean,
    isNextEnabled: Boolean,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // ۱. دکمه بازگشت (Back)
        // این دکمه فقط زمانی نشان داده می‌شود که در سوال اول (index = 0) نباشیم
        if (questionIndex > 0) {
            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, // پس‌زمینه شفاف برای دکمه قبلی
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "Back", fontSize = 16.sp)
            }
        } else {
            // اگر در سوال اول بودیم، یک فاصله خالی می‌گذاریم تا دکمه Next سمت راست باقی بماند
            Spacer(modifier = Modifier.width(1.dp))
        }

        // ۲. دکمه بعدی / پایان (Next / Finish)
        Button(
            onClick = onNextClick,
            enabled = isNextEnabled, // اگر کاربر ورودی را پر نکرده باشد، دکمه غیرفعال می‌شود
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)
            )
        ) {
            // اگر در سوال آخر باشیم متن دکمه Finish می‌شود، در غیر این صورت Next
            Text(
                text = if (isLastQuestion) "Finish" else "Next",
                fontSize = 16.sp
            )
        }
    }
}