package com.hajizade.lingualmate.presentation.main.chatDeteail.components

import androidx.compose.ui.tooling.preview.Preview



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hajizade.lingualmate.R
import com.hajizade.lingualmate.data.local.entity.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    user: UserProfile?,
    onNavigateBack: () -> Unit,
    onCallClick: () -> Unit,
    onVideoCallClick: () -> Unit,
    onBlockUserClick: () -> Unit,
    onClearChatClick: (deleteForEveryone: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onProfileClick:() -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showBlockDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        DeleteChatDialog(
            userName = user?.name ?: "User",
            onDismiss = { showDeleteDialog = false },
            onConfirmDelete = { deleteForEveryone ->
                onClearChatClick(deleteForEveryone)
            }
        )
    }

    if (showBlockDialog) {
        BlockUserDialog(
            userName = user?.name ?: "User",
            isBlocked = user?.isBlocked == true,
            onDismiss = { showBlockDialog = false },
            onConfirm = { onBlockUserClick() }
        )
    }

    TopAppBar(
        modifier = modifier,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.clickable { onProfileClick()}
            ) {
                if (!user?.profilePictureUri.isNullOrEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(user?.profilePictureUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                        error = painterResource(id = R.drawable.ic_launcher_foreground)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user?.name?.take(1)?.uppercase() ?: "?",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Column {
                    Text(
                        text = user?.name ?: "Loading...",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 17.sp
                    )

                    val statusText = if (user?.isOnline == true) {
                        "Online"
                    } else {
                        user?.lastSeen ?: ""
                    }
                    val statusColor = if (user?.isOnline == true) Color(0xFF4CAF50) else Color.Gray

                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodySmall,
                        color = statusColor,
                        fontSize = 12.sp
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onVideoCallClick) {
                Icon(imageVector = Icons.Default.Videocam, contentDescription = "Video Call")
            }
            IconButton(onClick = onCallClick) {
                Icon(imageVector = Icons.Default.Phone, contentDescription = "Call")
            }
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More Options")
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Clear Chat") },
                    leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) },
                    onClick = {
                        showMenu = false
                        showDeleteDialog = true
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = if (user?.isBlocked == true) "Unblock User" else "Block User",
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Block,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    onClick = {
                        showMenu = false
                        showBlockDialog = true
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}


