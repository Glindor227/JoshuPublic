package com.example.joshu.mvp.model.api.request.task

import com.example.joshu.mvp.model.entity.room.Task
import com.example.joshu.utils.SystemUtils

data class TaskUpdateRequest (
    val id: Int,
    val text: String,
    val priority: String,
    val dateTime: String,
    val folderId: Int,
    val status: String,
    val createDate: String,
    val edit: String = SystemUtils.formatDate(System.currentTimeMillis())
)

fun Task.mapToTaskUpdateRequest() = TaskUpdateRequest(
    id = this.id,
    text = this.text,
    priority = this.priority.toString(),
    dateTime = SystemUtils.formatDate(this.dateTime),
    folderId = this.folderId,
    status = this.status.toString(),
    createDate = SystemUtils.formatDate(this.createDate)
)