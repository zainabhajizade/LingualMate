package com.hajizade.lingualmate.presentation.main.profile.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileOptionsBottomSheet(
    onDismissRequest: () -> Unit,
    onEditProfileClick: () -> Unit,
    onSettingsClick: () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(bottom = 24.dp)
        ) {
            // گزینه Edit Profile
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDismissRequest()
                        onEditProfileClick()
                    }
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Edit Profile",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // گزینه Settings
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDismissRequest()
                        onSettingsClick()
                    }
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}