package com.example.joshu.mvp.model.entity

import com.example.joshu.mvp.model.entity.room.TaskFolder

object TaskFolderFactory {
    fun create(title: String, color: String): TaskFolder =
        TaskFolder(title = title, color = color)
    fun create(title: String, color: String, createDate: Long): TaskFolder =
        TaskFolder(title = title, color = color, createDate = createDate)
}