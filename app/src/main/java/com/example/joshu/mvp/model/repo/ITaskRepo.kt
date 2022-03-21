package com.example.joshu.mvp.model.repo

import com.example.joshu.mvp.model.entity.room.Task
import com.example.joshu.mvp.model.api.network.Result

interface ITaskRepo {
    suspend fun sendTask(task: Task): Result<Unit>

    suspend fun getAllTasks(date: Long): Result<List<Task>>

    suspend fun updateTask(task: Task): Result<Unit>

    suspend fun deleteTask(id: Int): Result<Unit>

    suspend fun transferTask(id: Int): Result<Unit>
}