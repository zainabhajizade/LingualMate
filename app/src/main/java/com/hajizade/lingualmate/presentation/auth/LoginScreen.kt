package com.hajizade.lingualmate.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // هدایت به صفحه بعدی در صورت موفقیت‌آمیز بودن لاگین
    LaunchedEffect(viewModel.isLoginSuccess) {
        if (viewModel.isLoginSuccess) {
            onLoginSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to LingualMate",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // فیلد ایمیل
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // فیلد پسورد
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // نمایش خطا
            if (viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // لودینگ یا دکمه‌ها
            if (viewModel.isLoading) {
                CircularProgressIndicator()
            } else {
                // دکمه ورود
                Button(
                    onClick = { viewModel.signIn() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // دکمه ثبت‌نام
                OutlinedButton(
                    onClick = { viewModel.signUp() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create New Account")
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(16.dp))

                // دکمه ورود با گوگل
                OutlinedButton(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                val webClientId = "537569643646-7hrtt0g8bndjkia7pf2v548u14fgv01i.apps.googleusercontent.com"

                                val googleIdOption = GetGoogleIdOption.Builder()
                                    .setServerClientId(webClientId)
                                    .setFilterByAuthorizedAccounts(false)
                                    .build()

                                val request = GetCredentialRequest.Builder()
                                    .addCredentialOption(googleIdOption)
                                    .build()

                                val credentialManager = CredentialManager.create(context)
                                val result = credentialManager.getCredential(context, request)
                                val credential = result.credential

                                if (credential is androidx.credentials.CustomCredential &&
                                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                                ) {
                                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                                    val idToken = googleIdTokenCredential.idToken

                                    // ارسال توکن به ویومدل
                                    viewModel.signInWithGoogle(idToken)
                                }
                            } catch (e: Exception) {
                                viewModel.errorMessage = e.localizedMessage ?: "Google sign in failed"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign in with Google")
                }
            }
        }
    }
}