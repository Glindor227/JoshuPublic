package com.example.joshu.mvp.model.repo

import com.example.joshu.mvp.model.api.network.Result
import com.example.joshu.mvp.model.entity.room.TaskFolder

interface ITaskFolderRepo {
    suspend fun sendTaskFolder(taskFolder: TaskFolder): Result<TaskFolder>

    suspend fun updateTaskFolder(taskFolder: TaskFolder): Result<Unit>

    suspend fun getAllTaskFolders(date: Long): Result<List<TaskFolder>>

    suspend fun deleteTaskFolder(id: Int): Result<Unit>
}