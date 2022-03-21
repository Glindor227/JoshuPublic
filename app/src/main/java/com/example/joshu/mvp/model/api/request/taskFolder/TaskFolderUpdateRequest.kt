package com.example.joshu.mvp.model.api.request.taskFolder

import com.example.joshu.mvp.model.api.request.task.mapToTaskCreateRequest
import com.example.joshu.mvp.model.entity.room.TaskFolder
import com.example.joshu.utils.SystemUtils

data class TaskFolderUpdateRequest (
    val title: String,
    val color: String,
    val createData: String,
    val edit: String
)

fun TaskFolder.mapToTaskFolderUpdateRequest(): TaskFolderUpdateRequest = TaskFolderUpdateRequest(
    title = this.title,
    color = this.color,
    createData = SystemUtils.formatDate(this.createDate),
    edit = SystemUtils.formatDate(this.editDate)
)