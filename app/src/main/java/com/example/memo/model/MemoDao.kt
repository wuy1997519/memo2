package com.example.memo.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MemoDao {
    @Query("select * from memo order by date desc")
    fun getAll(): LiveData<List<Memo>>

    @Query("select * from memo where `no` = :no")
    suspend fun getMemo(no: Int): Memo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(memo: Memo)

    @Update
    suspend fun update(memo: Memo)

    @Query("delete from memo where `no` = :no")
    suspend fun delete(no : Int)
}