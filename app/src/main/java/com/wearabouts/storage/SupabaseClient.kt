package com.wearabouts.supabase

// Supabase
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.Storage
import com.wearabouts.BuildConfig

// Log
import android.util.Log

object SupabaseClient {

    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(Storage)
    }
    
}