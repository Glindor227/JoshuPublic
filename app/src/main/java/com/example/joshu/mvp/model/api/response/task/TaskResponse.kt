package com.example.joshu.mvp.model.api.response.task

data class TaskResponse (
    val id: Int,
    val text: String?,
    val priority: String,
    val dateTime: String?,
    val folderId: Int,
    val status: Int,
    val repeatType: Int?,
    val createDate: String?,
    val edit: String?
)
