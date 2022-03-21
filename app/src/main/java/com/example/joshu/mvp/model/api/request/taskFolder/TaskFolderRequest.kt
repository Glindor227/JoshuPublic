package com.example.joshu.mvp.model.api.request.taskFolder

import com.example.joshu.mvp.model.api.request.task.mapToTaskCreateRequest
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.utils.SystemUtils

data class TaskFolderRequest (
    val id: Int,
    val title: String,
    val color: String,
    val createDate: String? = null
)

fun TaskFolder.mapToTaskFolderRequest() = TaskFolderRequest (
    id = this.id,
    title = this.title,
    color = this.color,
    createDate = SystemUtils.formatDate(this.createDate)
)

fun Long.millisToSeconds() = this / 1000

fun Long.secondToMillis() = this * 1000