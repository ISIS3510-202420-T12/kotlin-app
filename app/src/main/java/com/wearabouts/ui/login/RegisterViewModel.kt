package com.wearabouts.ui.login
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register(username: String, password: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading

            auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _registerState.value = RegisterState.Success
                    } else {
                        _registerState.value = RegisterState.Error(task.exception?.message ?: "Unknown error")
                    }
                }
        }
    }

    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        object Success : RegisterState()
        data class Error(val message: String) : RegisterState()
    }
}
