package com.kr.expenserecoder

import androidx.room.Entity
import androidx.room.PrimaryKey

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MyEntity::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun myDao(): MyDao
}


@Entity(tableName = "my_table")
data class MyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Float,
    val date : String,
)


