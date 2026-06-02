package com.kinmin.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "kinmin_session")

/** Holds the logged-in user id, name, and current check-in state. */
@Singleton
class SessionStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val userId = stringPreferencesKey("user_id")
    private val userName = stringPreferencesKey("user_name")
    private val checkedIn = booleanPreferencesKey("checked_in")

    val userIdFlow: Flow<String?> = context.dataStore.data.map { it[userId] }
    val userNameFlow: Flow<String?> = context.dataStore.data.map { it[userName] }
    val checkedInFlow: Flow<Boolean> = context.dataStore.data.map { it[checkedIn] ?: false }

    suspend fun saveUser(id: String, name: String) {
        context.dataStore.edit { it[userId] = id; it[userName] = name }
    }

    suspend fun setCheckedIn(value: Boolean) {
        context.dataStore.edit { it[checkedIn] = value }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
