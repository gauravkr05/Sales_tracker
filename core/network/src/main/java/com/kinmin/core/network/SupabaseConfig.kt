package com.kinmin.core.network

/**
 * Supabase credentials. We don't read app's BuildConfig here (module boundary),
 * so the app provides them at startup via SupabaseConfigHolder before the
 * client is first requested. Keeps :core:network independent of :app.
 */
data class SupabaseConfig(val url: String, val anonKey: String)

object SupabaseConfigHolder {
    @Volatile var config: SupabaseConfig =
        SupabaseConfig(url = "https://localhost.supabase.co", anonKey = "")
}
