package com.example.joshu.mvp.model.api.response.taskFolder

import com.google.gson.annotations.SerializedName

data class TaskFolderAllResponse (
    val data: List<TaskFolderResponse>?,
    val lastDate: Int
)