package com.kr.expenserecoder.ui.datastore


import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesManager(private val context: Context) {

    // Save values
    suspend fun saveUsername(username: String) {
        context.userPreferencesDataStore.edit { prefs ->
            prefs[PreferencesKeys.USERNAME] = username
        }
    }

    suspend fun saveMonthlyLimit(limit: Int) {
        context.userPreferencesDataStore.edit { prefs ->
            prefs[PreferencesKeys.MONTHLY_LIMIT] = limit
        }
    }


//    suspend fun saveAutoTracking(enabled: Boolean) {
//        context.userPreferencesDataStore.edit { prefs ->
//            prefs[PreferencesKeys.AUTO_TRACKING] = enabled
//        }
//    }



    // Read values
    val username: Flow<String> = context.userPreferencesDataStore.data.map { prefs ->
        prefs[PreferencesKeys.USERNAME] ?: ""
    }

    val monthlyLimit: Flow<Int> = context.userPreferencesDataStore.data.map { prefs ->
        prefs[PreferencesKeys.MONTHLY_LIMIT] ?: 0
    }

//    val autoTracking: Flow<Boolean> = context.userPreferencesDataStore.data.map { prefs ->
//        prefs[PreferencesKeys.AUTO_TRACKING] ?: false
//    }
}
