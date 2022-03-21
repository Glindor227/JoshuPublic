package com.example.joshu.mvp.model.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val text: String,
    val priority: Int,
    var dateTime: Long,
    var repeatType: Int,
    val folderId: Int,
    var status: Int,
    var deleteStatus: Int,
    var createDate: Long = System.currentTimeMillis(),
    var editDate: Long = 0L
)