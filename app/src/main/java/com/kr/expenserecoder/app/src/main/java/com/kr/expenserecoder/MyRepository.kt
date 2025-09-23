package com.kr.expenserecoder


import kotlinx.coroutines.flow.Flow

class MyRepository(private val dao: MyDao) {
    val allItems: Flow<List<MyEntity>> = dao.getAll()

    fun getAllByMonth(month: String): Flow<List<MyEntity>> {
        return dao.getAllByMonth(month)
    }

    suspend fun insert(item: MyEntity) {
        dao.insert(item)
    }

    suspend fun deleteAll(){
        dao.deleteAll()
        dao.resetAutoIncrement()
    }

    suspend fun deleteMonth(month: Int) {
        dao.deleteMonth(String.format("%02d", month)) // ensures "09", "10", etc.
    }


//    suspend fun deleteid(){
//        dao.deleteAll()
//        dao.resetAutoIncrement()
//    }

}
