package com.kr.expenserecoder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

    class MyViewModel(application: Application) : AndroidViewModel(application) {

        private val db = Room.databaseBuilder(
            application,
            MyDatabase::class.java,
            "my_database"
        )
            .fallbackToDestructiveMigration() // this will wipe old DB automatically
            .build()




        private val repository = MyRepository(db.myDao())

        val allItems = repository.allItems.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        // Current selected month (default = current month)
        private val _selectedMonth = MutableStateFlow(getCurrentDate(true))
        val selectedMonth = _selectedMonth.asStateFlow()

        // Expose items that match month
        val itemsByMonth = selectedMonth.flatMapLatest { month ->
            repository.getAllByMonth(month)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )


        fun insert(name: String, price: Float, date: String) {
            viewModelScope.launch {
                repository.insert(MyEntity(name = name, price = price, date = date))
            }
        }

        fun deleteall() {
            viewModelScope.launch {
                repository.deleteAll()
            }
        }

        fun deleteMonth(month: Int) {
            viewModelScope.launch {
                repository.deleteMonth(month)
            }
        }


//        fun deleteid() {
//            viewModelScope.launch {
//                repository.deleteAll()
//            }
//        }
    }

