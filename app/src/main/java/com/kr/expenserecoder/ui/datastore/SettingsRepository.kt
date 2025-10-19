package com.kr.expenserecoder.ui.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    val isAutoTrackingEnabled: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[PreferencesKeys.AUTO_TRACKING] ?: true }

    suspend fun setAutoTracking(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.AUTO_TRACKING] = enabled
        }
    }
}
