package com.kr.expenserecoder

import android.annotation.SuppressLint
import kotlinx.coroutines.flow.Flow

// MyRepository - Fixed
class MyRepository(private val dao: MyDao) {
    val allItems: Flow<List<Expense>> = dao.getAll()

    fun getExpensesByCategories(categories: List<ExpenseCategory>) =
        dao.getExpensesByCategories(categories)

    fun getAllByMonth(month: String): Flow<List<Expense>> {
        return dao.getAllByMonth(month)
    }

    suspend fun insert(expense: Expense) {
        dao.insert(expense)
    }

    suspend fun update(expense: Expense) {
        dao.updateExpense(expense)
    }


    suspend fun deleteAll() {
        dao.deleteAll()
        dao.resetAutoIncrement()
    }

    @SuppressLint("DefaultLocale")
    suspend fun deleteMonth(month: Int) {
        dao.deleteMonth(String.format("%02d", month))
    }
    suspend fun deleteItem(id: Long) {
        dao.deleteItem(id)
    }


}
