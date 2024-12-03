package com.wearabouts.ui.user

// Firestore
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

// Data state
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Data model
import com.wearabouts.models.User

// Pop-ups
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

// Log
import android.util.Log

class UserViewModel : ViewModel() {

    // Firestore
    private val db = Firebase.firestore

    // Users
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    init {
        getUsers()
    }

    // Get users
    fun getUsers() {
        viewModelScope.launch {
            try {
                db.collection("CleanUsers").get()
                    .addOnSuccessListener { result ->
                        val usersFetched = result.mapNotNull { document ->
                            // The document ID is the user email
                            document.toObject(User::class.java).copy(email = document.id)
                        }
                        _users.value = usersFetched
                    }
                    .addOnFailureListener {
                        Log.e("UserViewModel", "Error getting users", it)
                    }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error getting users", e)
            }
        }
    }

    // Add user
    fun addUser(user: User) {
        // User email is the ID
        // This function also works as update
        db.collection("users").document(user.email).set(user)
            .addOnSuccessListener {
                Log.d("UserViewModel", "User added")
            }
            .addOnFailureListener {
                Log.e("UserViewModel", "Error adding user", it)
            }
    }

    // Delete user
    fun deleteUser(user: User) {
        db.collection("users").document(user.email).delete()
            .addOnSuccessListener {
                Log.d("UserViewModel", "User deleted")
            }
            .addOnFailureListener {
                Log.e("UserViewModel", "Error deleting user", it)
            }
    }

}