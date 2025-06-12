package com.example.nanameue_code_test.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanameue_code_test.data.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    fun resetAuthState() {
        _authState.value = AuthState.Initial
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.signIn(email, password)
                .onSuccess {
                    _authState.value = AuthState.Success(it)
                }
                .onFailure {
                    _authState.value = AuthState.Error(it.message ?: "Authentication failed")
                }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.signUp(email, password)
                .onSuccess {
                    _authState.value = AuthState.Success(it)
                }
                .onFailure {
                    _authState.value = AuthState.Error(it.message ?: "Registration failed")
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
                .onSuccess {
                    _authState.value = AuthState.Initial
                }
                .onFailure {
                    _authState.value = AuthState.Error(it.message ?: "Sign out failed")
                }
        }
    }

    fun getUserInfo(): FirebaseUser? {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            return it
        } ?: run {
            // TODO need handle can't find user error!!
            return null
        }
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Success(val user: FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
}