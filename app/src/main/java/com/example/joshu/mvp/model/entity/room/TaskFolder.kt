package com.example.joshu.mvp.model.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_folder_table")
data class TaskFolder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val color: String,
    val createDate: Long = System.currentTimeMillis(),
    val editDate: Long = 0
)