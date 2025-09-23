package com.kr.expenserecoder

import androidx.room.TypeConverter


// RoomConverters.kt - Fixed
class RoomConverters {
    @TypeConverter
    fun fromExpenseCategory(category: ExpenseCategory): String = category.name

    @TypeConverter
    fun toExpenseCategory(categoryString: String): ExpenseCategory =
        try {
            ExpenseCategory.valueOf(categoryString)
        } catch (e: Exception) {
            ExpenseCategory.FOOD // Default fallback
        }
}