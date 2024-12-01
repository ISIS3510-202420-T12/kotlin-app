package com.wearabouts.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import android.content.Context

// Objects
import com.wearabouts.ui.login.SecureStorage

class RegisterViewModel : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun markErrorAsShown() {
        val currentState = _registerState.value
        if (currentState is RegisterState.Error) {
            _registerState.value = currentState.copy(shown = true)
        }
    }

    fun register(username: String, password: String, context: Context) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading

            auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        SecureStorage.saveLoginInfo(context, username, password)
                        _registerState.value = RegisterState.Success
                    } else {
                        _registerState.value = RegisterState.Error(task.exception?.message ?: "Unknown error")
                    }
                }
        }
    }

    fun resetState() {
        _registerState.value = RegisterState.Idle
    }

    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        object Success : RegisterState()
        data class Error(val message: String, val shown: Boolean = false) : RegisterState()    
    }
}

