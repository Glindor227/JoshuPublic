package com.example.joshu.mvp.model.api.response.task

data class AllTasksResponse (
    val data: List<TaskResponse>,
    val lastDate: Int
)