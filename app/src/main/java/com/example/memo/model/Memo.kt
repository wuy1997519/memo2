package com.example.memo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class Memo (
    @PrimaryKey(autoGenerate = true)
    var no: Int?,

    var title: String,

    var content: String,

    @ColumnInfo(name = "date")
    var datetime: Long
){
    constructor(): this(null, "","",0)
}