package com.kr.expenserecoder
//
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// DAO.kt - Fixed
@Dao
interface MyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC, time DESC")
    fun getAll(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE substr(date, 6, 2) = :month")
    fun getAllByMonth(month: String): Flow<List<Expense>>

    @Query("DELETE FROM expenses")
    suspend fun deleteAll()

    @Query("DELETE FROM expenses WHERE substr(date, 6, 2) = :month")
    suspend fun deleteMonth(month: String)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteItem(id: Long)

    @Query("DELETE FROM sqlite_sequence WHERE name='expenses'")
    suspend fun resetAutoIncrement()
}
