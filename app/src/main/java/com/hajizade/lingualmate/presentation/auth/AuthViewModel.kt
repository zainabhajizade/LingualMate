package com.hajizade.lingualmate.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hajizade.lingualmate.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Input fields and screen states
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)

    var isLoading by mutableStateOf(false)
        private set

    var isLoginSuccess by mutableStateOf(false)
        private set

    // Sign in with Email and Password
    fun signIn() {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Please enter email and password"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = authRepository.signInWithEmail(email, password)

            isLoading = false
            if (result.isSuccess) {
                isLoginSuccess = true
            } else {
                errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Login failed"
            }
        }
    }

    // Sign up with Email and Password
    fun signUp() {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Please enter email and password"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = authRepository.signUpWithEmail(email, password)

            isLoading = false
            if (result.isSuccess) {
                isLoginSuccess = true
            } else {
                errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Sign up failed"
            }
        }
    }

    // Sign in with Google
    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = authRepository.signInWithGoogle(idToken)

            isLoading = false
            if (result.isSuccess) {
                isLoginSuccess = true
            } else {
                errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Google sign in failed"
            }
        }
    }
}