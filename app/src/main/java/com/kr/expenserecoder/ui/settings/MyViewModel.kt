package com.kr.expenserecoder.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.kr.expenserecoder.Expense
import com.kr.expenserecoder.ExpenseCategory
import com.kr.expenserecoder.MyDatabase
import com.kr.expenserecoder.MyRepository
import com.kr.expenserecoder.getCurrentDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.kr.expenserecoder.ui.datastore.UserPreferencesManager

val Application.dataStore by preferencesDataStore(name = "settings")
private val AUTO_TRACKING_KEY = booleanPreferencesKey("auto_tracking")


// MyViewModel - Fixed
class MyViewModel(application: Application) : AndroidViewModel(application ) {

    private val db = Room.databaseBuilder(
        application,
        MyDatabase::class.java,
        "my_database"
    )
        .fallbackToDestructiveMigration()
        .build()

    private val repository = MyRepository(db.myDao())

    // ✅ DataStore reference
    private val dataStore = application.dataStore

    private val _showSplash = MutableStateFlow(true)
    val showSplash: StateFlow<Boolean> = _showSplash

    fun hideSplash() {
        _showSplash.value = false
    }


    // Flow to observe auto-tracking setting
    val isAutoTrackingEnabled: Flow<Boolean> = dataStore.data
        .map { prefs -> prefs[AUTO_TRACKING_KEY] ?: true } // default true

    // Save auto-tracking preference
    fun setAutoTracking(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[AUTO_TRACKING_KEY] = enabled
            }
        }
    }

    // ------------------- Existing DB logic -------------------


    var allItems = repository.allItems.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        emptyList()
    )


    private val _selectedMonth = MutableStateFlow(getCurrentDate(true))
    val selectedMonth = _selectedMonth.asStateFlow()

//    val itemsByMonth = selectedMonth.flatMapLatest { month ->
//        repository.getAllByMonth(month)
//    }.stateIn(
//        viewModelScope,
//        SharingStarted.Companion.WhileSubscribed(5000),
//        emptyList()
//    )

    fun insert(amount: Double, category: ExpenseCategory, description: String, date :String) {
        viewModelScope.launch {
            repository.insert(
                Expense(
                    amount = amount,
                    category = category,
                    description = description,
                    date = date
                )
            )
        }
    }

    fun updateExpense(id: Long, amount: Double, category: ExpenseCategory, description: String, date: String
    ) {
        viewModelScope.launch {
            repository.update(
                Expense(
                    id = id,
                    amount = amount,
                    category = category,
                    description = description,
                    date = date
                )
            )
        }
    }


    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun deleteMonth(month: Int) {
        viewModelScope.launch {
            repository.deleteMonth(month)
        }
    }

    fun deletItem(id: Long) {
        viewModelScope.launch {
            repository.deleteItem(id)
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            allItems = repository.allItems.stateIn(
                viewModelScope,
                SharingStarted.Companion.WhileSubscribed(5000),
                emptyList()
            )
        }
    }


    // Track selected categories
    private val _selectedCategories = MutableStateFlow<List<ExpenseCategory>>(emptyList())
    val selectedCategories: StateFlow<List<ExpenseCategory>> = _selectedCategories

    val filteredExpenses: StateFlow<List<Expense>> = _selectedCategories
        .flatMapLatest { categories ->
            if (categories.isEmpty()) {
                // If no category selected, return all items
                repository.allItems
            } else {
                repository.getExpensesByCategories(categories)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun toggleCategory(category: ExpenseCategory) {
        _selectedCategories.update { current ->
            if (category in current) current - category else current + category
        }
    }

    // ----- User Settings (DataStore) -----
    private val prefs = UserPreferencesManager(application)

    val username = prefs.username.stateIn(viewModelScope, SharingStarted.Lazily, "")
    val monthlyLimit = prefs.monthlyLimit.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    fun updateUsername(name: String) = viewModelScope.launch {
        prefs.saveUsername(name)
    }

    fun updateMonthlyLimit(limit: Int) = viewModelScope.launch {
        prefs.saveMonthlyLimit(limit)
    }



}