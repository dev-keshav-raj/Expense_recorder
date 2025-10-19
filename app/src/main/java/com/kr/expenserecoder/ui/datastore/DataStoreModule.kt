package com.kr.expenserecoder.ui.datastore

// com.kr.expenserecoder.ui.datastore.DataStoreModule.kt

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.userPreferencesDataStore by preferencesDataStore("user_prefs")
