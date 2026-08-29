package com.hajizade.lingualmate.presentation.main.community

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hajizade.lingualmate.data.local.entity.UserProfile
import com.hajizade.lingualmate.presentation.main.components.UserCard
import com.hajizade.lingualmate.ui.theme.LingualMateTheme

@Composable
fun CommunityScreen(
    onUserClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CommunityViewModel = hiltViewModel()
) {
    val state by viewModel.State.collectAsState()

    // صدا زدن بخش اصلی UI
    CommunityContent(
        state = state,
        onUserClick = onUserClick,
        modifier = modifier
    )
}

// 👈 این بخش همان محتوای صفحه است که هم در اپلیکیشن و هم در Preview استفاده می‌شود
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityContent(
    state: CommunityUiState,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Community",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            },
            actions = {
                Surface(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(36.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Z",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.users.isEmpty() -> {
                    Text(
                        text = "No users found in the community.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.users) { user ->
                            UserCard(
                                user = user,
                                onClick = {
                                    // 👈 لاگ اضافه شده برای بررسی مقدار شناسه هنگام کلیک
                                    Log.d("ChatDebug", "Clicked user -> id: '${user.id}', name: '${user.name}'")
                                    onUserClick(user.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}