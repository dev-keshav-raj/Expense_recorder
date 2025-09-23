package com.kr.expenserecoder

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MyEntity)

    @Query("SELECT * FROM my_table")
    fun getAll(): Flow<List<MyEntity>>

    @Query("SELECT * FROM my_table WHERE substr(date, 4, 2) = :month")
    fun getAllByMonth(month: String = getCurrentDate(true).toString()): Flow<List<MyEntity>>


    @Query("DELETE  FROM my_table")
    suspend fun deleteAll()

    @Query("DELETE FROM my_table WHERE substr(date, 4, 2) = :month")
    suspend fun deleteMonth(month: String)


//    @Query("DELETE  FROM my_table Where id = :id")
//     fun deleteID(id : Int)

    @Query("DELETE FROM sqlite_sequence WHERE name='my_table'")
    suspend fun resetAutoIncrement()


}
