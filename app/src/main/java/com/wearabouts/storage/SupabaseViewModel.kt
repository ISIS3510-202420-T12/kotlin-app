package com.wearabouts.storage

// Data state
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context

// Supabase
import com.wearabouts.supabase.SupabaseClient.client
import io.github.jan.supabase.storage.storage

// Pop-ups
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

// Log
import android.util.Log

class SupabaseViewModel : ViewModel() {

    // Example repository using Supabase storage, with supabase version 1.3.2
    // https://github.com/YoursSohail/LearnSupabase/blob/storage/app/src/main/java/com/yourssohail/learnsupabase/SupabaseViewModel.kt

    val backgroundsBucket = "backgrounds"
    val clothingsBucket = "clothings"
    val usersBucket = "usericons"

    private val projectId = "zoinycbesbrgjevnkomf"

    private val TAG = "supabase"

    // Backgrounds
    private val _backgrounds = MutableStateFlow<List<String>>(emptyList())
    val backgrounds: StateFlow<List<String>> = _backgrounds

    init {
        
    }

    // Utils

    fun constructUrl(bucket: String, path: String): String {
        return "https://$projectId.supabase.co/storage/v1/object/public/$bucket/$path"
    }

    // Uploads

    fun upload(bucket: String, filepath: String, file: ByteArray) {
        // filepath must include the file name and extension, with folder structure if needed by "/" separator
        viewModelScope.launch {
            try {
                client.storage.from(bucket).upload(filepath, file)
            } catch (e: Exception) {
                Log.e(TAG, "Error uploading file", e)
            }
        }
    }

    fun fetchBackgrounds() {
        viewModelScope.launch {
            try {
                // Get all files in the bucket, this returns List<FileObject>
                val files = client.storage.from(backgroundsBucket).list()
                // Convert List<FileObject> to List<String> of urls
                val paths = files.map { file -> file.name }
                // Get the urls
                val urls = paths.map { path -> constructUrl(backgroundsBucket, path) }
                _backgrounds.value = urls
            } catch (e: Exception) {
                Log.e(TAG, "Error getting backgrounds", e)
            }
        }
    }
    
}