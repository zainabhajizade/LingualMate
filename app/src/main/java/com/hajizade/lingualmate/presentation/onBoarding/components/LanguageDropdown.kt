package com.hajizade.lingualmate.presentation.onBoarding.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hajizade.lingualmate.domain.model.LanguageData
import com.hajizade.lingualmate.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDropdown(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        // کادری که کاربر روی صفحه می‌بیند
        OutlinedTextField(
            value = selectedLanguage,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Select a language...", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        // منوی کشویی که باز می‌شود
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            LanguageData.languages.forEach { language ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // نمایش پرچم با اندازه بزرگتر (۲۴sp) دقیقا شبیه ایموجی تصویر
                            Text(text = language.flagEmoji, fontSize = 24.sp)

                            Spacer(modifier = Modifier.width(12.dp))

                            // نمایش نام زبان
                            Text(text = language.name, fontSize = 16.sp)
                        }
                    },
                    onClick = {
                        // ذخیره پرچم و نام با هم (مثلا: "🇩🇪 German (Deutsch)")
                        onLanguageSelected("${language.flagEmoji}  ${language.name}")
                        expanded = false
                    }
                )
            }
        }
    }
}