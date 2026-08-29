package com.hajizade.lingualmate.presentation.onBoarding.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hajizade.lingualmate.domain.model.ProficiencyLevel
import com.hajizade.lingualmate.domain.model.SelectedLanguage
import com.hajizade.lingualmate.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageWithLevelSelector(
    selectedLanguages: List<SelectedLanguage>,
    onAddLanguage: (SelectedLanguage) -> Unit,
    onRemoveLanguage: (SelectedLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    var tempLanguage by remember { mutableStateOf("") }
    var tempLevel by remember { mutableStateOf(ProficiencyLevel.BEGINNER) }
    var levelDropdownExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {

        // --- ۱. ورودی انتخاب زبان جدید و سطح ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Dropdown زبان (وزن بیشتر)
            Box(modifier = Modifier.weight(1.5f)) {
                LanguageDropdown(
                    selectedLanguage = tempLanguage,
                    onLanguageSelected = { tempLanguage = it }
                )
            }

            // Dropdown انتخاب سطح تسلط
            ExposedDropdownMenuBox(
                expanded = levelDropdownExpanded,
                onExpandedChange = { levelDropdownExpanded = !levelDropdownExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = tempLevel.name,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = levelDropdownExpanded) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = levelDropdownExpanded,
                    onDismissRequest = { levelDropdownExpanded = false }
                ) {
                    ProficiencyLevel.entries.forEach { level ->
                        DropdownMenuItem(
                            text = { Text(text = level.name, fontSize = 14.sp) },
                            onClick = {
                                tempLevel = level
                                levelDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // دکمه افزودن (+)
            IconButton(
                onClick = {
                    if (tempLanguage.isNotBlank()) {
                        onAddLanguage(SelectedLanguage(tempLanguage, tempLevel))
                        tempLanguage = "" // ریست کردن برای انتخاب بعدی
                    }
                },
                enabled = tempLanguage.isNotBlank(),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Language")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- ۲. لیست زبان‌های انتخاب شده ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            selectedLanguages.forEach { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = item.languageName,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Level: ${item.level.name}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        }

                        IconButton(onClick = { onRemoveLanguage(item) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}