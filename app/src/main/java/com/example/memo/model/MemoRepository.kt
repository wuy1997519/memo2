package com.example.memo.model

import android.app.Application
import androidx.lifecycle.LiveData

class MemoRepository (application: Application){
    private val memoDatabase = MemoDatabase.getDatabase(application)
    private val memoDao: MemoDao = memoDatabase!!.memoDao()

    val allMemo: LiveData<List<Memo>> = memoDao.getAll()

    suspend fun getMemo(no: Int): Memo {
        return memoDao.getMemo(no)
    }

    suspend fun insert(memo: Memo){
        memoDao.insert(memo)
    }

    suspend fun update(memo: Memo){
        memoDao.update(memo)
    }

    suspend fun delete(no: Int){
        memoDao.delete(no)
    }
}