package com.wearabouts.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun markErrorAsShown() {
        val currentState = _loginState.value
        if (currentState is LoginState.Error) {
            _loginState.value = currentState.copy(shown = true)
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _loginState.value = LoginState.Success
                    } else {
                        val errorMessage = when (val exception = task.exception) {
                            is FirebaseAuthInvalidUserException -> {
                                when (exception.errorCode) {
                                    "ERROR_USER_NOT_FOUND" -> "No account found with this email"
                                    "ERROR_USER_DISABLED" -> "This account has been disabled"
                                    else -> "Invalid email address"
                                }
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                when (exception.errorCode) {
                                    "ERROR_WRONG_PASSWORD" -> "Incorrect password"
                                    "ERROR_INVALID_EMAIL" -> "Invalid email format"
                                    else -> "Invalid email or password"
                                }
                            }
                            else -> exception?.message ?: "An unknown error occurred"
                        }
                        _loginState.value = LoginState.Error(errorMessage)
                    }
                }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String, val shown: Boolean = false) : LoginState()    
    }
}
