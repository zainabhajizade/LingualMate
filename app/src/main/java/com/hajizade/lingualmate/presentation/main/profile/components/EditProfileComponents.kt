package com.hajizade.lingualmate.presentation.main.profile.components


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ProfileImagePicker(
    profilePictureUri: String?,
    onImageSelected: (String) -> Unit
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it.toString()) }
    }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .size(100.dp)
            .clickable { photoPickerLauncher.launch("image/*") }
    ) {
        AsyncImage(
            model = profilePictureUri,
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Change Photo",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDropdown(
    label: String,
    selectedLanguage: String,
    languages: List<String>,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedLanguage,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language) },
                    onClick = {
                        onLanguageSelected(language)
                        isExpanded = false
                    }
                )
            }
        }
    }
}