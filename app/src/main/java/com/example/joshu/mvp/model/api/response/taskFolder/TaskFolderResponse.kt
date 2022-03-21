package com.example.joshu.mvp.model.api.response.taskFolder

import com.example.joshu.mvp.model.api.request.taskFolder.secondToMillis
import com.example.joshu.mvp.model.entity.room.TaskFolder

data class TaskFolderResponse (
    val id: Int,
    val title: String?,
    val color: String?,
    val createDate: String?,
    val edit: String?
)

fun TaskFolderResponse.toTaskFolder(): TaskFolder = TaskFolder(
    id = this.id,
    title = this.title ?: "",
    color = this.color ?: "#FFFFFF",
    createDate = this.createDate?.toLong()?.secondToMillis() ?: 0L,
    editDate = this.edit?.toLong()?.secondToMillis() ?: 0L
)