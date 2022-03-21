package com.example.joshu.mvp.model.repo

import com.example.joshu.mvp.model.ISharedPreferences
import com.example.joshu.mvp.model.api.ITaskApiService
import com.example.joshu.mvp.model.api.network.Result
import com.example.joshu.mvp.model.api.network.error.NetworkParserError
import com.example.joshu.mvp.model.api.request.task.mapToTaskCreateRequest
import com.example.joshu.mvp.model.api.request.task.mapToTaskUpdateRequest
import com.example.joshu.mvp.model.api.request.taskFolder.millisToSeconds
import com.example.joshu.mvp.model.api.request.taskFolder.secondToMillis
import com.example.joshu.mvp.model.entity.DateRepeatEnum
import com.example.joshu.mvp.model.entity.DeleteStatusEnum
import com.example.joshu.mvp.model.entity.room.Task

class TaskRepoImpl(private val apiService: ITaskApiService,
               private val settings: ISharedPreferences,
               networkParserError: NetworkParserError) : BaseRepo(networkParserError), ITaskRepo {

    override suspend fun sendTask(task: Task): Result<Unit> {
        val accessToken = "Bearer ${settings.accessToken}"
        val request = task.mapToTaskCreateRequest()
        val response = safeApiCall (call = { apiService.taskCreate(accessToken, request) })
        return when(response) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> response
        }
    }

    override suspend fun getAllTasks(date: Long): Result<List<Task>> {
        val accessToken = "Bearer ${settings.accessToken}"
        val response = safeApiCall (call = { apiService.taskGetAll(accessToken, date.millisToSeconds()) })
        return when (response) {
            is Result.Success -> {
                val list = response.data.data.map {
                    Task(id = it.id, text = it.text ?: "",
                        priority = it.priority.toInt(),
                        dateTime = it.dateTime?.toLong()?.secondToMillis() ?: System.currentTimeMillis(),
                        repeatType = it.repeatType ?: DateRepeatEnum.DEFAULT.value,
                        folderId = it.folderId,
                        status = it.status,
                        deleteStatus = DeleteStatusEnum.DEFAULT.value,
                        createDate = it.createDate?.toLong()?.secondToMillis() ?: System.currentTimeMillis(),
                        editDate = it.edit?.toLong()?.secondToMillis() ?: 0
                        )}
                Result.Success(list)
            }
            is Result.Error -> response
        }
    }

    override suspend fun updateTask(task: Task): Result<Unit> {
        val accessToken = "Bearer ${settings.accessToken}"
        val request = task.mapToTaskUpdateRequest()
        val response = safeApiCall (call = { apiService.putTask(accessToken, task.id, request) })
        return when (response) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> response
        }
    }

    override suspend fun deleteTask(id: Int): Result<Unit> {
        val accessToken = "Bearer ${settings.accessToken}"
        val response = safeApiCall (call = { apiService.deleteTask(accessToken, id) })
        return when (response) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> response
        }
    }

    override suspend fun transferTask(id: Int): Result<Unit> {
        val accessToken = "Bearer ${settings.accessToken}"
        val response = safeApiCall (call = { apiService.transferTask(accessToken, id) })
        return when (response) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> response
        }
    }
}