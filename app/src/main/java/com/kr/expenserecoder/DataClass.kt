package com.kr.expenserecoder

import android.os.Build
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

// Use same Expense class as in UI, but mark as Entity
// Data class with proper API level handling
@Entity(tableName = "expenses")
data class Expense constructor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val category: ExpenseCategory,
    val description: String,
    val date: String ,
    val time: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) LocalTime.now().toString() else "12:00:00"
) {
    // Helper properties for UI
    val localDate: LocalDate?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try { LocalDate.parse(date) } catch (e: Exception) { null }
        } else null

    val localTime: LocalTime?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try { LocalTime.parse(time) } catch (e: Exception) { null }
        } else null
}