package com.kr.expenserecoder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// MyViewModel - Fixed
class MyViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        MyDatabase::class.java,
        "my_database"
    )
        .fallbackToDestructiveMigration()
        .build()

    private val repository = MyRepository(db.myDao())

    val allItems = repository.allItems.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _selectedMonth = MutableStateFlow(getCurrentDate(true))
    val selectedMonth = _selectedMonth.asStateFlow()

    val itemsByMonth = selectedMonth.flatMapLatest { month ->
        repository.getAllByMonth(month)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun insert(amount: Double, category: ExpenseCategory, description: String,date :String) {
        viewModelScope.launch {
            repository.insert(
                Expense(amount = amount, category = category, description = description , date = date)
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
}