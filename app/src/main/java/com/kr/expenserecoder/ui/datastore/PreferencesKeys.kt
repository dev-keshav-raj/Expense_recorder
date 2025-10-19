package com.kr.expenserecoder.ui.datastore

//import androidx.datastore.preferences.core.booleanPreferencesKey
//
//object PreferencesKeys {
//    val AUTO_TRACKING = booleanPreferencesKey("auto_tracking")
//}

// com.kr.expenserecoder.ui.datastore.PreferencesKeys.kt


// com.kr.expenserecoder.ui.datastore.PreferencesKeys.kt

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val AUTO_TRACKING = booleanPreferencesKey("auto_tracking")
    val USERNAME = stringPreferencesKey("username")
    val MONTHLY_LIMIT = intPreferencesKey("monthly_limit")
}
